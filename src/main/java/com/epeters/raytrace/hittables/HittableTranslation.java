package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.utils.Vector;

public class HittableTranslation implements Hittable {

    private final Hittable target;
    private final Vector offset;
    private final Box bounds;

    public HittableTranslation(Hittable target, Vector offset) {
        this.target = target;
        this.offset = offset;
        this.bounds = target.getBounds().translate(offset);
    }

    @Override
    public Box getBounds() {
        return bounds;
    }

    public Hit intersect(Ray originalRay, double tmin, double tmax) {

        if (bounds.doesNotIntersect(originalRay, tmin, tmax)) {
            return null;
        }

        // translate the incoming ray and apply hit calculation
        Ray translatedRay = translate(originalRay, true);
        Hit translatedHit = target.intersect(translatedRay, tmin, tmax);
        if (translatedHit == null) {
            return null;
        }

        // on the way back out, "untranslate" the hit point
        return new Hit(
                translatedHit.t(),
                translate(translatedHit.point(), false),
                translatedHit.normal(),
                translatedHit.front(),
                translatedHit.material(),
                translatedHit.u(),
                translatedHit.v());
    }

    protected Ray translate(Ray ray, boolean forward) {
        return new Ray(translate(ray.origin(), forward), ray.direction());
    }

    protected Vector translate(Vector point, boolean forward) {
        return forward ? point.minus(offset) : point.plus(offset);
    }
}
