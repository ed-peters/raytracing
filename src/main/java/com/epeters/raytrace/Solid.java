package com.epeters.raytrace;

import com.epeters.raytrace.geometry.Geometry;
import com.epeters.raytrace.geometry.Sphere;
import com.epeters.raytrace.material.Material;

import java.util.List;

/**
 * Combination of geometry and material
 *
 * @param geometry the shape of the solid
 * @param material the material of the solid
 */
public record Solid(Geometry geometry, Material material) {

    /**
     * @param ray a ray of interest
     * @param tmin minimum hit distance
     * @param tmax maximum hit distance
     * @return a {@link Hit} calculated using the underlying geometry (null if this is a miss)
     */
    public Hit hit(Ray ray, double tmin, double tmax) {
        double t = geometry.hit(ray, tmin, tmax);
        return Double.isNaN(t) ? null : Hit.from(this, ray, t);
    }

    /**
     * @return the closest hit among the specified solids (null if this is a miss)
     */
    static Hit computeHit(List<Solid> solids, Ray ray) {
        double tmin = 0.000001f;
        double tcur = Double.MAX_VALUE;
        Hit best = null;
        for (Solid solid : solids) {
            Hit hit = solid.hit(ray, tmin, tcur);
            if (hit != null) {
                best = hit;
                tcur = hit.t();
            }
        }
        return best;
    }

    /**
     * @return a spherical solid with the supplied properties
     */
    static Solid sphere(Vector center, double radius, Material material) {
        return new Solid(new Sphere(center, radius), material);
    }
}
