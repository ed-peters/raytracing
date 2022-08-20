package com.epeters.raytrace;

import static com.epeters.raytrace.Utils.scaleInt;
import static com.epeters.raytrace.Utils.sqrt;

/**
 * Three-dimensional double vector
 */
public record Vector(double x, double y, double z) {

    public static final Vector ORIGIN = new Vector(0.0, 0.0, 0.0);

    public Vector negate() {
        return new Vector(-x, -y, -z);
    }

    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector minus(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector mul(double f) {
        return new Vector(x * f, y * f, z * f);
    }

    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double square() {
        return dot(this);
    }

    public Vector div(double f) {
        return new Vector(x / f, y / f, z / f);
    }

    public double mag() {
        return sqrt(square());
    }

    public Vector normalize() {
        return div(mag());
    }

    public boolean isOpposite(Vector other) {
        return dot(other) < 0.0f;
    }

    public int [] toRgb() {
        return new int[]{
                scaleInt(x, 255),
                scaleInt(y, 255),
                scaleInt(z, 255) };
    }

    public Vector randomHemisphericBounce() {
        Vector next = randomOnUnitSphere();
        if (!isOpposite(next)) {
            next = next.negate();
        }
        return next;
    }

    public static Vector randomInUnitSphere() {
        while (true) {
            Vector next = Utils.randomVector(-1.0f, 1.0f);
            if (next.square() < 1.0f) {
                return next;
            }
        }
    }

    public static Vector randomOnUnitSphere() {
        while (true) {
            Vector next = Utils.randomVector(-1.0f, 1.0f);
            if (next.square() < 1.0f) {
                return next.normalize();
            }
        }
    }
}
