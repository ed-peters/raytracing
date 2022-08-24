package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Hittable} that represents a collection of other {@link Hittable}
 * objects and knows how to compute hits over them all.
 */
public class HittableList implements Hittable {

    private final List<Hittable> hittables;
    private final BoundingBox bounds;

    public HittableList(List<? extends Hittable> c) {
        this.hittables = new ArrayList<>(c);
        this.bounds = new BoundingBox(c.stream().map(Hittable::getBounds).collect(Collectors.toList()));
    }

    @Override
    public BoundingBox getBounds() {
        return bounds;
    }

    @Override
    public Hit hit(Ray ray, double tmin, double tmax) {

        if (!getBounds().hit(ray, tmin, tmax)) {
            return null;
        }

        double tcur = Double.MAX_VALUE;
        Hit best = null;
        for (Hittable hittable : hittables) {
            Hit hit = hittable.hit(ray, tmin, tcur);
            if (hit != null) {
                best = hit;
                tcur = hit.t();
            }
        }
        return best;
    }
}
