package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.randomUnitVector;

public interface Material {

    Bounce computeBounce(Hit hit);

    static Material lambertian(double r, double g, double b) {
        final Vector color = new Vector(r, g, b);
        return (hit) -> new Bounce(color, hit.normal().plus(randomUnitVector()));
    }

    static Material metal(double r, double g, double b, double f) {
        final Vector color = new Vector(r, g, b);
        return (hit) -> new Bounce(color, hit.incoming().reflect(hit.normal(), f));
    }
}
