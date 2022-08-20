package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Utils;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.clamp;

public class Metal extends AbstractColoredMaterial {

    private final double fuzz;

    public Metal(Vector color) {
        this(color, 0.0);
    }

    public Metal(Vector color, double fuzz) {
        super(color);
        this.fuzz = clamp(fuzz, 0.0, 1.0);
    }

    @Override
    protected Vector computeBounceDirection(Hit hit) {
        Vector direction = reflect(hit.ray().direction().normalize(), hit.normal());
        if (fuzz > 0.0) {
            direction = direction.plus(randomFuzz());
        }
        return direction;
    }

    private Vector reflect(Vector incoming, Vector hitNormal) {
        double doubleb = 2.0 * incoming.dot(hitNormal);
        return incoming.minus(hitNormal.mul(doubleb));
    }

    private Vector randomFuzz() {
        while (true) {
            Vector next = Utils.randomVector(-1.0f, 1.0f);
            if (next.square() < 1.0f) {
                return next.mul(fuzz);
            }
        }
    }
}
