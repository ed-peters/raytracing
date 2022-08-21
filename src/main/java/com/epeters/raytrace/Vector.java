package com.epeters.raytrace;

import static com.epeters.raytrace.Utils.randomUnitVector;
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

    public Vector mul(Vector other) {
        return new Vector(x * other.x, y * other.y, z * other.z);
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
}
