package com.epeters.raytrace;

import java.util.List;

/**
 * Interface for an object that knows how to determine whether it's hit by
 * a particular ray.
 */
public interface Hittable {

    Hit hit(Ray ray, float tmin, float tmax);

    static Hit computeHit(List<Hittable> hittables, Ray ray) {
        float tmin = 0.000001f;
        float tcur = Float.MAX_VALUE;
        Hit best = null;
        for (Hittable solid : hittables) {
            Hit hit = solid.hit(ray, tmin, tcur);
            if (hit != null) {
                best = hit;
                tcur = hit.t();
            }
        }
        return best;
    }
}
