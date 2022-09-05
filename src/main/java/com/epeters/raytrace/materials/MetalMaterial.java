package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.textures.Texture;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.textures.Texture.solid;
import static com.epeters.raytrace.utils.Utils.clamp;

/**
 * Implements metal reflection with fuzzing
 * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#metal">guide</a>
 */
public final class MetalMaterial implements Material {

    private final Texture texture;
    private final double fuzz;

    public MetalMaterial(Vector color, double fuzz) {
        this(solid(color), fuzz);
    }

    public MetalMaterial(Texture texture, double fuzz) {
        this.texture = texture;
        this.fuzz = clamp(fuzz, 0.0, 1.0);
    }

    @Override
    public HitColor computeHitColor(MaterialParams params) {
        Vector color = texture.calculateColor(params.point(), params.u(), params.v());
        Vector bounce =  params.incoming().reflect(params.normal(), fuzz);
        return HitColor.bounced(color, bounce);
    }
}
