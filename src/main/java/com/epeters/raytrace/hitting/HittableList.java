package com.epeters.raytrace.hitting;

import com.epeters.raytrace.solids.Ray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of a collection of {@link Hittable} instances. Knows how to compute a
 * bounding box over all of them, and to determine the closest hit for a {@link Ray}.
 */
public class HittableList extends ArrayList<Hittable> implements Hittable {

    private final BoundingBox boundingBox;

    public HittableList(Collection<? extends Hittable> c) {
        super(c);
        boundingBox = BoundingBox.combine(stream().map(h -> h.getBoundingBox()).collect(Collectors.toList()));
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public Hit computeHit(Ray ray, double tmin, double tmax) {
        double tcur = Double.MAX_VALUE;
        Hit best = null;
        for (Hittable next : this) {
            Hit hit = next.computeHit(ray, tmin, tcur);
            if (hit != null) {
                best = hit;
                tcur = hit.t();
            }
        }
        return best;
    }
}
