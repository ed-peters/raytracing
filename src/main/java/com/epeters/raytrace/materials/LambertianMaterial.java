package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitDetails;
import com.epeters.raytrace.utils.Mector;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.textures.Texture;

import static com.epeters.raytrace.utils.Utils.randomUnitVector;

/**
 * Implements "true" Lambertian reflection for a matte material with an arbitrary texture
 * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials/analternativediffuseformulation">guide</a>
 */
public class LambertianMaterial implements Material {

    private final Texture texture;

    public LambertianMaterial(Vector color) {
        this(Texture.solid(color));
    }

    public LambertianMaterial(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Vector computeAttenuation(HitDetails hit) {
        return texture.calculateColor(hit.point(), hit.textureCoords());
    }

    @Override
    public Vector computeBounce(HitDetails hit) {
        return hit.normal().plus(randomUnitVector());
    }
}
