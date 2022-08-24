package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;

import java.util.function.Supplier;

/**
 * Represents the intersection of a ray with an object
 *
 * @param ray the ray in question
 * @param t the distance along the ray of the intersection
 * @param info something that can supply additional into about the intersection if asked
 */
public record Hit(Ray ray, double t, Supplier<HitInfo> info) {
}
