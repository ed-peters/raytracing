package com.epeters.raytrace.solids;

import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Represents a ray in 3D space, characterized by an origin and a direction. Direction
 * is NOT normalized. Knows how to calculate a point along the line.
 *
 * @param origin the origin of the ray
 * @param direction the direction of the ray (unit vector)
 * @param time the time along the render period (0.0 - 1.0)
 */
public record Ray(Vector origin, Vector direction, double time) {

    public Ray(Vector origin, Vector direction, double time) {
        this.origin = origin;
        this.direction = direction.normalize();
        this.time = time;
    }

    public Vector at(double t) {
        return origin.plus(direction.mul(t));
    }

    public static Ray from(double x, double y, double z) {
        return new Ray(ORIGIN, vec(x, y, z), 0.0);
    }
}
