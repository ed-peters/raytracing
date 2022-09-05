package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.textures.Texture;

/**
 * Interface for a component that knows how to compute the scattering of light
 * around a hit point.
 */
public interface Material {

    HitColor computeHitColor(MaterialParams params);

    static Material lambertian(Vector color) {
        return new LambertianMaterial(color);
    }

    static Material lambertian(Texture texture) {
        return new LambertianMaterial(texture);
    }

    static Material metal(Vector color, double fuzz) {
        return new MetalMaterial(color, fuzz);
    }

    static Material dialectric(double backRatio) {
        return new ClearMaterial(backRatio);
    }

    static Material light(Vector color) { return new LightedMaterial(Texture.solid(color)); }
}
