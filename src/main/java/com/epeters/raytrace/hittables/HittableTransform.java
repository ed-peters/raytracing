package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.utils.Vector;

public abstract class HittableTransform implements Hittable {

    private final Box bounds;
    private final Hittable target;

    protected HittableTransform(Box bounds, Hittable target) {
        this.bounds = bounds;
        this.target = target;
    }

    public Hit intersect(Ray originalRay, double tmin, double tmax) {

        if (bounds.doesNotIntersect(originalRay, tmin, tmax)) {
            return null;
        }

        // we transform the incoming ray into the new coordinate system and
        // then apply the target's intersection logic
        Ray transformedRay = transformRay(originalRay);
        Hit transformedHit = target.intersect(transformedRay, tmin, tmax);
        if (transformedHit == null) {
            return null;
        }

        // to compute the color, we have to transform both the point of
        // intersection and the incoming ray
        return new Hit(originalRay, transformedHit.t(), (point, incoming) -> {
            Vector transformedPoint = transformPoint(point);
            return transformedHit.color().apply(transformedPoint, transformedRay.direction());
        });
    }

    protected Ray transformRay(Ray originalRay) {
        return new Ray(transformPoint(originalRay.origin()), originalRay.direction());
    }

    protected abstract Vector transformPoint(Vector point);
}
