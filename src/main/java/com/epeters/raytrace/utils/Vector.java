package com.epeters.raytrace.utils;

import static com.epeters.raytrace.utils.Utils.randomUnitVector;

import static com.epeters.raytrace.utils.Utils.sqrt;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.max;

/**
 * Three-dimensional double vector
 */
public record Vector(double x, double y, double z) {

    public static final Vector ORIGIN = new Vector(0.0, 0.0, 0.0);
    public static final Vector MIN = new Vector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    public static final Vector MAX = new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    public double component(Axis which) {
        return switch (which) {
            case X -> x();
            case Y -> y();
            case Z -> z();
        };
    }

    public Vector negate() {
        return vec(-x, -y, -z);
    }

    public Vector plus(Vector other) {
        return vec(x + other.x, y + other.y, z + other.z);
    }

    public Vector minus(Vector other) {
        return vec(x - other.x, y - other.y, z - other.z);
    }

    public Vector mul(Vector other) {
        return vec(x * other.x, y * other.y, z * other.z);
    }

    public Vector mul(double f) {
        return vec(x * f, y * f, z * f);
    }

    public Vector div(double f) {
        return mul(1.0 / f);
    }

    public Vector cross(Vector other) {
        double x = y() * other.z() - z() * other.y();
        double y = z() * other.x() - x() * other.z();
        double z = x() * other.y() - y() * other.x();
        return vec(x, y, z);
    }

    public double dot(Vector other) {
        return Utils.dot(x, y, z, other.x, other.y, other.z);
    }

    public double square() {
        return dot(this);
    }

    public double length() {
        return sqrt(square());
    }

    public Vector normalize() {
        return mul(1.0 / length());
    }

    public boolean isOpposite(Vector other) {
        return dot(other) < 0.0f;
    }

    public Vector minWith(Vector other) {
        return vec(min(x, other.x), min(y, other.y), min(z, other.z));
    }

    public Vector maxWith(Vector other) {
        return vec(max(x, other.x), max(y, other.y), max(z, other.z));
    }

    /**
     * @return a random unit vector that bounces "against" this one
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials/analternativediffuseformulation">doc</a>
     */
    public Vector randomHemisphericReflection() {
        Vector next = randomUnitVector();
        return dot(next) < 0.0 ? next.negate() : next;
    }

    public boolean equalish(Vector other) {
        return abs(x - other.x) < 0.000001
                && abs(y - other.y) < 0.000001
                && abs(z - other.z) < 0.000001;
    }

    public boolean zeroish() {
        return abs(x) < 1e-8 && abs(y) < 1e-8 && abs(z) < 1e-8;
    }

    public static Vector vec(double x, double y, double z) {
        return new Vector(x, y, z);
    }
}
