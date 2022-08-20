package com.epeters.raytrace;

import java.util.function.Function;

/**
 * Represents the intersection of a ray with a surface
 *
 * @param point the point of intersection
 * @param normal the outward surface normal at the point
 * @param t the distance along the ray of the point
 * @param front did the ray hit the front of the face?
 */
public record Hit(Vector point, Vector normal, double t, boolean front) {

    public static Hit from(Ray ray, double t, Function<Vector,Vector> func) {
        Vector point = ray.at(t);
        Vector normal = func.apply(point).normalize();
        boolean front = ray.direction().isOpposite(normal);
        return new Hit(point, front ? normal : normal.negate(), t, front);
    }
}
