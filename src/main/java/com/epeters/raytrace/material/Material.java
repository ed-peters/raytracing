package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Vector;

public interface Material {

    Bounce computeBounce(Hit hit);

    static Material lambertian(double r, double g, double b) {
        return new LambertianMatte(new Vector(r, g, b));
    }

    static Material metal(double r, double g, double b) {
        return new Metal(new Vector(r, g, b));
    }
}
