package com.epeters.raytrace;

import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Represents a ray in 3D space, characterized by an origin and a direction. Direction
 * is NOT normalized. Knows how to calculate a point along the line.
 *
 * @param origin the origin of the ray
 * @param direction the direction of the ray (unit vector)
 */
public record Ray(Vector origin, Vector direction) {

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Ray(double x, double y, double z) {
        this(ORIGIN, vec(x, y, z));
    }

    public Vector at(double t) {
        return origin.plus(direction.mul(t));
    }
}
