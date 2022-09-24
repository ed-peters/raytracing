package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.utils.Vector;

/**
 * Encapsulates everything we want to know about the intersection of a ray and an object
 */
public record Hit(double t, Vector point, Vector normal, boolean front, Material material, double u, double v) {

    public Hit flipped() {
        return new Hit(t, point, normal, !front, material, u, v);
    }

    public static Hit from(Ray ray, double t, Vector normal, Material material, double u, double v) {
        boolean front = ray.direction().isOpposite(normal);
        return new Hit(t, ray.at(t), front ? normal : normal.negate(), front, material, u, v);
    }

    public static Hit from(Ray ray, double t, Vector point, Vector normal, Material material, double u, double v) {
        boolean front = ray.direction().isOpposite(normal);
        return new Hit(t, point, front ? normal : normal.negate(), front, material, u, v);
    }
}
