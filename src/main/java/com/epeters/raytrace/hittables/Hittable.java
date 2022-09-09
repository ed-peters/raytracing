package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Axis;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.utils.Vector;

/**
 * Generic interface for "something that can be hit by a {@link Ray}". This might be a
 * solid object or a more abstract bounding volume.
 */
public interface Hittable {

    Box getBounds();

    Hit intersect(Ray ray, double tmin, double tmax);

    default Hittable translate(Vector offset) {
        Box bounds = getBounds().translate(offset);
        return new HittableTransform(bounds, this) {
            @Override
            public Box getBounds() {
                return bounds;
            }
            @Override
            protected Vector transformPoint(Vector point) {
                return point.minus(offset);
            }
        };
    }

    default Hittable rotate(Axis axis, double degrees) {
        Box bounds = getBounds().rotate(axis, degrees);
        double cos = Math.cos(Math.toRadians(degrees));
        double sin = Math.sin(Math.toRadians(degrees));
        return new HittableTransform(bounds, this) {
            @Override
            public Box getBounds() {
                return bounds;
            }
            @Override
            protected Ray transformRay(Ray originalRay) {
                Vector to = transformPoint(originalRay.origin());
                Vector td = transformPoint(originalRay.direction());
                return new Ray(to, td);
            }
            @Override
            protected Vector transformPoint(Vector point) {
                return point.rotateY(sin, cos);
            }
        };
    }
}
