package com.epeters.raytrace.solids;

import com.epeters.raytrace.hitting.Hit;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.square;
import static com.epeters.raytrace.utils.Vector.vec;

public class LambertianMaterial implements Material {

    private final Vector color;

    public LambertianMaterial(Vector color) {
        this.color = color;
    }

    @Override
    public Scatter computeScatter(Hit hit) {

        double ux = 0.0;
        double uy = 0.0;
        double uz = 0.0;
        do {
            ux = random(-1.0, 1.0);
            uy = random(-1.0, 1.0);
            uz = random(-1.0, 1.0);
        } while (square(ux, uy, uz) > 1.0);

        double bounceX = hit.normal().x();
        double bounceY = hit.normal().y();
        double bounceZ = hit.normal().z();

        return new Scatter(color, vec(bounceX, bounceY, bounceZ));
    }
}
