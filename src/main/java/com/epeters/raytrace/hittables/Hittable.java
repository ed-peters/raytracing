package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.solids.Solid;
import com.epeters.raytrace.utils.Vector;

/**
 * Generic interface for "something that can be hit by a {@link Ray}". This might be a
 * {@link Solid} or a more abstract bounding volume.
 */
public abstract class Hittable {

    private final BoundingBox bounds;

    protected Hittable(BoundingBox bounds) {
        this.bounds = bounds;
    }

    /** @return the bounding box for this hittable (this will be called a lot) */
    public final BoundingBox getBounds() {
        return bounds;
    }

    /** @retyrn a new {@link Hit} if this ray hit something; null otherwise */
    public final Hit hit(Ray ray, double tmin, double tmax) {
        if (bounds != null && !bounds.intersects(ray, tmin, tmax)) {
            return null;
        }
        return computeHit(ray, tmin, tmax);
    }

    protected abstract Hit computeHit(Ray ray, double tmin, double tmax);

    public static Hittable translate(Hittable target, Vector offset) {
        return new TranslatedHittable(target, offset);
    }

    public static Hittable rotateY(Hittable target, double angle) {
        return new RotatedHittable(target, RotatedHittable.Axis.Y, angle);
    }
}
