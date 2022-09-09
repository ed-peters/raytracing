package com.epeters.raytrace;

import com.epeters.raytrace.utils.Vector;

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

    public Vector at(double t) {
        return origin.plus(direction.mul(t));
    }
}
