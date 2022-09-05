package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.randomUnitVector;

public final class IsotropicMaterial implements Material {

    private final Vector color;

    public IsotropicMaterial(Vector color) {
        this.color = color;
    }

    @Override
    public HitColor computeHitColor(MaterialParams params) {
        return HitColor.bounced(color, randomUnitVector());
    }
}
