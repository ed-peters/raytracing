package com.epeters.raytrace.geometry;

import com.epeters.raytrace.Solid;
import com.epeters.raytrace.utils.Vector;

/**
 * Represents the intersection of a ray with an object
 *
 * @param ray the ray in question
 * @param object the object in question
 * @param t the distance along the ray of the intersection
 * @param point the point of intersection
 * @param normal the outward surface normal at the point (unit vector)
 * @param front did the ray hit the front of the face?
 */
public record Hit(Ray ray, Solid object, double t, Vector point, Vector normal, boolean front) {

    /**
     * @return normalized direction of the incoming ray
     */
    public Vector incoming() {
        return ray.direction();
    }

    /**
     * @return the hit calculated from the supplied parameters
     */
    public static Hit from(Solid object, Ray ray, double t) {
        Vector point = ray.at(t);
        Vector normal = object.geometry().computeNormal(point);
        boolean front = ray.direction().isOpposite(normal);
        return new Hit(ray, object, t, point, front ? normal : normal.negate(), front);
    }
}
