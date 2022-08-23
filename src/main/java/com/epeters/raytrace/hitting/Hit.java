package com.epeters.raytrace.hitting;

import com.epeters.raytrace.solids.Ray;
import com.epeters.raytrace.solids.Solid;
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
}
