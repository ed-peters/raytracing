package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Box;

public class HittableFlip implements Hittable {

    private final Hittable target;
    private final Box bounds;

    public HittableFlip(Hittable target) {
        this.target = target;
        this.bounds = target.getBounds();
    }

    @Override
    public Box getBounds() {
        return bounds;
    }

    @Override
    public Hit intersect(Ray ray, double tmin, double tmax) {
        Hit hit = target.intersect(ray, tmin, tmax);
        if (hit == null) {
            return null;
        }
        return hit.flipped();
    }
}
