package com.epeters.raytrace.solids;

import com.epeters.raytrace.hitting.Hit;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.clamp;
import static com.epeters.raytrace.utils.Utils.dot;
import static com.epeters.raytrace.utils.Utils.mag;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.square;
import static com.epeters.raytrace.utils.Vector.vec;

public class MetalMaterial implements Material {

    private final Vector color;
    private final double fuzz;

    public MetalMaterial(Vector color, double fuzz) {
        this.color = color;
        this.fuzz = clamp(fuzz, 0.0, 1.0);
    }

    @Override
    public Scatter computeScatter(Hit hit) {

        Vector rd = hit.ray().direction();
        Vector hn = hit.normal();

        double bmag = dot(rd.x(), rd.y(), rd.z(), hn.x(), hn.y(), hn.z());
        double bounceX = -hn.x() * bmag;
        double bounceY = -hn.y() * bmag;
        double bounceZ = -hn.z() * bmag;

        if (fuzz > 0.0) {

            double ux = 0.0;
            double uy = 0.0;
            double uz = 0.0;
            do {
                ux = random(-1.0, 1.0);
                uy = random(-1.0, 1.0);
                uz = random(-1.0, 1.0);
            } while (square(ux, uy, uz) > 1.0);

            double umag = mag(ux, uy, uz);

            bounceX += ux * umag * fuzz;
            bounceY += uy * umag * fuzz;
            bounceZ += uz * umag * fuzz;
        }

        return new Scatter(color, vec(bounceX, bounceY, bounceZ));
    }
}
