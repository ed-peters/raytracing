package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Utils;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.MID_GRAY;

/**
 * Hemispherical diffusion - uses a bounce vector located on the unit sphere
 * going away from the object.
 */
public class HemisphericalMatte implements Material {

    @Override
    public Bounce computeBounce(Hit hit) {
        Vector hitNormal = hit.normal();
        Vector bounceDirection = hitNormal.plus(randomBounce());
        if (bounceDirection.isOpposite(hitNormal)) {
            bounceDirection = bounceDirection.negate();
        }
        return new Bounce(MID_GRAY, bounceDirection);
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
