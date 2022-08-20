package com.epeters.raytrace;

import com.epeters.raytrace.geometry.Geometry;
import com.epeters.raytrace.material.Material;

import java.util.List;

/**
 * Combination of geometry and material
 *
 * @param geometry the shape of the solid
 * @param material the material of the solid
 */
public record Solid(Geometry geometry, Material material) {

    public Solid(Geometry geometry) {
        this(geometry, null);
    }

    public Hit hit(Ray ray, double tmin, double tmax) {
        double t = geometry.hit(ray, tmin, tmax);
        return Double.isNaN(t) ? null : Hit.from(this, ray, t);
    }

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
}
