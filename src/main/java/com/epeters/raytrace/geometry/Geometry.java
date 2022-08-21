package com.epeters.raytrace.geometry;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.Vector;

/**
 * Interface for an object that knows how to determine whether it's hit by
 * a particular ray, and the surface normal at the point of intersection.
 */
public interface Geometry {

    /**
     * @param point the point of intersection
     * @return the outward surface normal (unit vector) at the point of intersection
     */
    Vector computeNormal(Vector point);

    /**
     * @param ray a ray of interest
     * @param tmin minimum hit distance
     * @param tmax maximum hit distance
     * @return t-value for intersection, if there is one within the supplied bounds
     * ({@link Double#NaN} if there isn't one
     */
    double hit(Ray ray, double tmin, double tmax);
}
