package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Vector;

public abstract class AbstractColoredMaterial implements Material {

    private final Vector color;

    protected AbstractColoredMaterial(Vector color) {
        this.color = color;
    }

    public Vector getColor() {
        return color;
    }


    @Override
    public Bounce computeBounce(Hit hit) {
        return new Bounce(getColor(), computeBounceDirection(hit));
    }

    protected abstract Vector computeBounceDirection(Hit hit);
}
