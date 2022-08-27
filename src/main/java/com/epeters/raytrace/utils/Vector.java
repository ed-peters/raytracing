package com.epeters.raytrace.utils;

import static com.epeters.raytrace.utils.Utils.randomUnitVector;
import static com.epeters.raytrace.utils.Utils.scaleInt;
import static com.epeters.raytrace.utils.Utils.sqrt;

import static java.lang.Math.min;
import static java.lang.Math.max;

/**
 * Three-dimensional double vector
 */
public record Vector(double x, double y, double z) {

    public static final Vector ORIGIN = new Vector(0.0, 0.0, 0.0);

    public double component(int which) {
        return switch (which) {
            case 0 -> x();
            case 1 -> y();
            case 2 -> z();
            default -> throw new IllegalArgumentException("unknown component "+which);
        };
    }

    public Vector negate() {
        return new Vector(-x, -y, -z);
    }

    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector minus(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector mul(Vector other) {
        return new Vector(x * other.x, y * other.y, z * other.z);
    }

    public Vector mul(double f) {
        return new Vector(x * f, y * f, z * f);
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

    public Vector minWith(Vector other) {
        return vec(min(x, other.x), min(y, other.y), min(z, other.z));
    }

    public Vector maxWith(Vector other) {
        return vec(max(x, other.x), max(y, other.y), max(z, other.z));
    }

    public int toRgb() {
        int r = scaleInt(x(), 255);
        int g = scaleInt(y(), 255);
        int b = scaleInt(z(), 255);
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        return 0xFF000000 | r | g | b;
    }

    /**
     * @return a random unit vector that bounces "against" this one
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials/analternativediffuseformulation">doc</a>
     */
    public Vector randomHemisphericReflection() {
        Vector next = randomUnitVector();
        return next.isOpposite(this) ? next : next.negate();
    }

    /**
     * @return the result of reflecting this vector against the supplied normal, with optional fuzz
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#metal/mirroredlightreflection">doc</a>
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#metal/fuzzyreflection">doc</a>
     */
    public Vector reflect(Vector normal, double fuzz) {
        Vector result = minus(normal.mul(2.0 * dot(normal)));
        if (fuzz > 0.0) {
            result = result.plus(randomUnitVector().mul(fuzz));
        }
        return result;
    }

    public static Vector vec(double x, double y, double z) {
        return new Vector(x, y, z);
    }
}
