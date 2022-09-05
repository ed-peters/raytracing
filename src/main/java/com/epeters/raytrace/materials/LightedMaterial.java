package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.textures.Texture;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.textures.Texture.solid;

public final class LightedMaterial implements Material {

    private final Texture texture;

    public LightedMaterial(Vector color) {
        this(solid(color));
    }

    public LightedMaterial(Texture texture) {
        this.texture = texture;
    }

    @Override
    public HitColor computeHitColor(MaterialParams params) {
        Vector color = texture.calculateColor(params.point(), params.u(), params.v());
        return HitColor.lit(color);
    }
}
