package com.epeters.raytrace;

import static com.epeters.raytrace.Vector.ORIGIN;

/**
 * Represents a ray in 3D space, characterized by an origin and a direction. Direction
 * is NOT normalized. Knows how to calculate a point along the line.
 */
public record Ray(Vector origin, Vector direction) {

    public Ray(double x, double y, double z) {
        this(ORIGIN, new Vector(x, y, z));
    }

    public Vector at(double t) {
        return origin.plus(direction.mul(t));
    }
}
