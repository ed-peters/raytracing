package com.epeters.raytrace.solids;

import com.epeters.raytrace.hitting.Hit;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.clamp;
import static com.epeters.raytrace.utils.Utils.randomUnitVector;

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
     * @param color attenuation color
     * @return a {@link Material} for calculating scatter parameters
     */
    static Material lambertian(Vector color) {
        return new LambertianMaterial(color);
    }

    /**
     * Implements reflection for metal materials with optional fuzzing
     *
     * @param color attenuation color
     * @param fuzz optional fuzz
     * @return a {@link Material} for calculating scatter parameters
     */
    static Material metal(Vector color, double fuzz) {
        return new MetalMaterial(color, fuzz);
    }

    /**
     * {@link DialectricMaterial}
     */
    static Material dialectric(double backRatio) {
        return new DialectricMaterial(backRatio);
    }
}
