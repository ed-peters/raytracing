package com.epeters.raytrace.surfaces;

import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.surfaces.Texture.solid;
import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Utils.randomUnitVector;
import static com.epeters.raytrace.utils.Vector.vec;

/**
 * A material knows how to calculate a full {@link HitColor} - that is, surface lighting including
 * emitted light and potential diffusion via bouncing.
 */
public interface Material {

    /**
     * The main method for all materials to implement
     */
    HitColor computeHitColor(MaterialParams params);

    static Material norm() {
        return (p) -> {
            Vector color = vec(p.incoming().x() + 1.0, p.incoming().y() + 1.0, p.incoming().z() + 1.0);
            return HitColor.bounced(color.mul(0.5), null);
        };
    }

    /**
     * See {@link #lambertian(Texture)}
     */
    static Material lambertian(Vector color) {
        return lambertian(solid(color));
    }

    /**
     * Uses Lambertian diffusion to calculate the bouncing of the light ray off a matte surface
     */
    static Material lambertian(Texture texture) {
        return (params) -> {
            Vector color = texture.calculateColor(params.point(), params.u(), params.v());
            Vector bounce = params.normal().plus(randomUnitVector());
            return HitColor.bounced(color, bounce);
        };
    }

    /**
     * See {@link #metal(Texture, double)}
     */
    static Material metal(Vector color, double fuzz) {
        return metal(solid(color), fuzz);
    }

    /**
     * Isotropic material (used for fog etc.)
     */
    static Material isotropic(Vector color) {
        return (params) -> {
            Vector bounce = randomUnitVector();
            return HitColor.bounced(color, bounce);
        };
    }

    /**
     * Reflective surface with the supplied amount of "fuzz" (0.0 means shiny)
     */
    static Material metal(Texture texture, double fuzz) {
        return (params) -> {
            Vector color = texture.calculateColor(params.point(), params.u(), params.v());
            Vector bounce =  params.incoming().reflect(params.normal(), fuzz);
            return HitColor.bounced(color, bounce);
        };
    }

    /**
     * Refractive surface (glass etc.)
     */
    static Material dialectric(double indexOfRefraction) {
        double frontRatio = 1.0 / indexOfRefraction;
        return (params) -> {
            double ratio = params.front() ? frontRatio : indexOfRefraction;
            Vector bounce = params.incoming().refract(params.normal(), ratio);
            return HitColor.bounced(WHITE, bounce);
        };
    }

    /**
     * Lighted surface
     */
    static Material light(Vector color) {
        return (params) -> HitColor.lit(color);
    }
}
