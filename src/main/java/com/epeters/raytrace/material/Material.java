package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.clamp;
import static com.epeters.raytrace.Utils.randomUnitVector;
import static com.epeters.raytrace.Vector.vec;

/**
 * Interface for a component that knows how to compute the scattering of light
 * around a hit point.
 */
public interface Material {

    /**
     * @param hit an intersection
     * @return the resulting scattering parameters
     */
    Scatter computeScatter(Hit hit);

    /**
     * Implements "true" Lambertian reflection for a matte material
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials/analternativediffuseformulation">guide</a>
     *
     * @param r attenuation color
     * @param g attenuation color
     * @param b attenuation color
     * @return a {@link Material} for calculating scatter parameters
     */
    static Material lambertian(double r, double g, double b) {
        final Vector color = vec(r, g, b);
        return (hit) -> new Scatter(color, hit.normal().plus(randomUnitVector()));
    }

    /**
     * Implements reflection for metal materials with optional fuzzing
     *
     * @param r attenuation color
     * @param g attenuation color
     * @param b attenuation color
     * @param f optional fuzz
     * @return a {@link Material} for calculating scatter parameters
     */
    static Material metal(double r, double g, double b, double f) {
        final Vector color = vec(r, g, b);
        final double fuzz = clamp(f, 0.0, 1.0);
        return (hit) -> new Scatter(color, hit.incoming().reflect(hit.normal(), fuzz));
    }

    static Material dialectric(double backRatio) {
        return new Dialectric(backRatio);
    }
}
