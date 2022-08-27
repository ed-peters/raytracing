package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitDetails;
import com.epeters.raytrace.textures.Texture;
import com.epeters.raytrace.utils.Vector;

public class LightedMaterial implements Material {

    private final Texture texture;

    public LightedMaterial(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Vector computeEmission(HitDetails hit) {
        return texture.calculateColor(hit.point(), hit.textureCoords());
    }

    @Override
    public Vector computeAttenuation(HitDetails hit) {
        return null;
    }

    @Override
    public Vector computeBounce(HitDetails hit) {
        return null;
    }
}
