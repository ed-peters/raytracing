package com.epeters.raytrace.surfaces;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Scatter;
import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.surfaces.Texture.solid;
import static com.epeters.raytrace.utils.Utils.randomUnitVector;

/**
 * A material knows how to calculate a full {@link Scatter} - that is, surface lighting including
 * emitted light and potential diffusion via bouncing.
 */
public interface Material {

    Scatter computeScatter(Ray ray, Hit hit);

    default double computeScatterPdf(Ray ray, Hit hit, Ray scatter) {
        return 0.0;
    }

    static Material norm() {
        return (ray, hit) -> null;
    }

    /**
     * See {@link #lambertian(Texture)}
     */
    static Material lambertian(Color color) {
        return lambertian(solid(color));
    }

    /**
     * Uses Lambertian diffusion to calculate the bouncing of the light ray off a matte surface
     */
    static Material lambertian(Texture texture) {
        return new MaterialLambertian(texture);
    }

    /**
     * See {@link #metal(Texture, double)}
     */
    static Material metal(Color color, double fuzz) {
        return metal(solid(color), fuzz);
    }

    /**
     * Isotropic material (used for fog etc.)
     */
    static Material isotropic(Color color) {
        return (ray, hit) -> {
            Vector bounce = randomUnitVector();
            return Scatter.simpleBounce(color, bounce);
        };
    }

    /**
     * Reflective surface with the supplied amount of "fuzz" (0.0 means shiny)
     */
    static Material metal(Texture texture, double fuzz) {
        return new MaterialMetal(texture, fuzz);
    }

    /**
     * Refractive surface (glass etc.)
     */
    static Material dialectric(double indexOfRefraction) {
        return new MaterialDialectric(indexOfRefraction);
    }

    /**
     * Lighted surface
     */
    static Material light(Color color) {
        return (ray, hit) -> Scatter.emissive(color);
    }
}
