package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.solids.Solid;

import java.util.function.Supplier;

/**
 * Represents the intersection of a ray with an object. We don't compute all the details (e.g.
 * point of intersection, bounce ray) at first, because we might not need to.
 *
 * @param ray the ray in question
 * @param t the distance along the ray of the intersection
 * @param solid the object that was intersected
 */
public record Hit(Ray ray, double t, Solid solid) {
}
