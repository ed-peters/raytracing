package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Vector;

public class Metal extends AbstractColoredMaterial {

    public Metal(Vector color) {
        super(color);
    }

    @Override
    protected Vector computeBounceDirection(Hit hit) {
        Vector incoming = hit.ray().direction().normalize();
        Vector hitNormal = hit.normal();
        double doubleb = 2.0 * incoming.dot(hitNormal);
        return incoming.minus(hitNormal.mul(doubleb));
    }
}
