package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Utils;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.MID_GRAY;

/**
 * Lambertian diffusion - uses a bounce vector located on the unit sphere.
 */
public class LambertianMatte extends AbstractColoredMaterial {

    public LambertianMatte() {
        this(MID_GRAY);
    }

    public LambertianMatte(Vector color) {
        super(color);
    }

    @Override
    protected Vector computeBounceDirection(Hit hit) {
        return hit.normal().plus(randomBounce());
    }

    private Vector randomBounce() {
        while (true) {
            Vector next = Utils.randomVector(-1.0f, 1.0f);
            if (next.square() < 1.0f) {
                return next.normalize();
            }
        }
    }
}
