package com.epeters.raytrace.utils;

import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static java.lang.Math.sqrt;
import static com.epeters.raytrace.utils.Utils.random;

import static com.epeters.raytrace.utils.Vector.vec;

public final class Mector {

    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    public Mector() {
        this(ORIGIN);
    }

    public Mector(Vector v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public Mector plus(Vector v) {
        x += v.x();
        y += v.y();
        z += v.z();
        return this;
    }

    public Mector plusTimes(Vector v, double factor) {
        x += v.x() * factor;
        y += v.y() * factor;
        z += v.z() * factor;
        return this;
    }

    public Mector plusTimes(Vector v, Vector f) {
        x += v.x() * f.x();
        y += v.y() * f.y();
        z += v.z() * f.z();
        return this;
    }

    public Mector minus(double dx, double dy, double dz) {
        x -= dx;
        y -= dy;
        z -= dz;
        return this;
    }

    public Mector minus(Vector v) {
        x -= v.x();
        y -= v.y();
        z -= v.z();
        return this;
    }

    public Mector div(double factor) {
        mul(1.0 / factor);
        return this;
    }

    public Mector mul(double factor) {
        x *= factor;
        y *= factor;
        z *= factor;
        return this;
    }

    public Mector mul(Vector v) {
        x *= v.x();
        y *= v.y();
        z *= v.z();
        return this;
    }

    public Mector negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public double dot(Vector v) {
        return Utils.dot(x, y, z, v.x(), v.y(), v.z());
    }

    public double square() {
        return Utils.dot(x, y, z);
    }

    public Mector normalize() {
        mul(1.0 / sqrt(square()));
        return this;
    }

    public Mector randomInsideUnitCube() {
        x = random(-1.0, 1.0);
        y = random(-1.0, 1.0);
        z = random(-1.0, 1.0);
        return this;
    }

    public Mector randomInsideUnitCircle() {
        do {
            randomInsideUnitCube();
        } while (square() > 1.0);
        return this;
    }

    public Mector randomUnit() {
        return randomInsideUnitCircle().normalize();
    }

    public Vector toVector() {
        return vec(x, y, z);
    }
}
