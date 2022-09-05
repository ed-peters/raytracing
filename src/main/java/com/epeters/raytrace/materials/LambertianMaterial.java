package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.textures.Texture;

import static com.epeters.raytrace.textures.Texture.solid;
import static com.epeters.raytrace.utils.Utils.randomUnitVector;

/**
 * Implements "true" Lambertian reflection for a matte material with an arbitrary texture
 * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials/analternativediffuseformulation">guide</a>
 */
public final class LambertianMaterial implements Material {

    private final Texture texture;

    public LambertianMaterial(Vector color) {
        this(solid(color));
    }

    public LambertianMaterial(Texture texture) {
        this.texture = texture;
    }

    @Override
    public HitColor computeHitColor(MaterialParams params) {
        Vector color = texture.calculateColor(params.point(), params.u(), params.v());
        Vector bounce = params.normal().plus(randomUnitVector());
        return HitColor.bounced(color, bounce);
    }
}
