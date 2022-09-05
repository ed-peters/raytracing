package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Vector;

public final class TranslatedHittable extends Hittable {

    private final Hittable target;
    private final Vector offset;

    public TranslatedHittable(Hittable target, Vector offset) {
        super(makeBounds(target.getBounds(), offset));
        this.target = target;
        this.offset = offset;
    }

    @Override
    protected Hit computeHit(Ray originalRay, double tmin, double tmax) {

        // we transform the incoming ray into the new coordinate system and
        // then apply the target's intersection logic
        Ray transformedRay = transformRay(originalRay);
        Hit transformedHit = target.hit(transformedRay, tmin, tmax);
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

    private Ray transformRay(Ray originalRay) {
        return new Ray(transformPoint(originalRay.origin()), originalRay.direction());
    }

    private Vector transformPoint(Vector point) {
        return point.minus(offset);
    }

    private static BoundingBox makeBounds(BoundingBox original, Vector offset) {
        if (original == null) {
            return null;
        }
        return new BoundingBox(
                original.getMin().plus(offset),
                original.getMax().plus(offset));
    }
}
