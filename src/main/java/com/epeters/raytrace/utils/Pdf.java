package com.epeters.raytrace.utils;

import com.epeters.raytrace.hittables.Hittable;

import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.randomCosineDirection;

public interface Pdf {

    /**
     * @return a random vector for scattering
     */
    Vector generate();

    /**
     * @return the value of the PDF for the supplied scattering direction
     */
    double value(Vector direction);

    /**
     * Generates random scattering based on cosine sampling
     */
    static Pdf cosine(Vector w) {
        Basis basis = Basis.fromW(w);
        return new Pdf() {
            @Override
            public Vector generate() {
                return basis.local(randomCosineDirection());
            }
            @Override
            public double value(Vector direction) {
                double cos = direction.normalize().dot(basis.w());
                return (cos <= 0) ? 0 : cos / Math.PI;
            }
        };
    }

    /**
     * Generates scattering towards a {@link Hittable} like a light
     */
    static Pdf hittable(Hittable hittable, Vector origin) {
        return new Pdf() {
            @Override
            public double value(Vector direction) {
                return hittable.pdfValue(origin, direction);
            }

            @Override
            public Vector generate() {
                return hittable.directionTowards(origin);
            }
        };
    }

    static Pdf mix(Pdf p0, Pdf p1) {
        return new Pdf() {
            @Override
            public Vector generate() {
                return random() < 0.5 ? p0.generate() : p1.generate();
            }

            @Override
            public double value(Vector direction) {
                double v0 = p0.value(direction);
                double v1 = p1.value(direction);
                return (v0 + v1) / 2.0;
            }
        };
    }
}
