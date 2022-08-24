package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitInfo;
import com.epeters.raytrace.Vector;

/**
 * Interface for a component that knows how to compute the scattering of light
 * around a hit point.
 */
public interface Material {

    /** Computes attenuation and bounce given the supplied hit info */
    void computeScatter(HitInfo hit);

    static Material lambertian(Vector color) {
        return new LambertianMaterial(color);
    }

    static Material metal(Vector color, double fuzz) {
        return new MetalMaterial(color, fuzz);
    }

    static Material dialectric(double backRatio) {
        return new DialectricMaterial(backRatio);
    }
}
