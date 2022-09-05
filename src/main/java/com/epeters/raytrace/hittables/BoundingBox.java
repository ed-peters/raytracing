package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Vector;

import java.util.List;

/**
 * Represents an axis-aligned bounding box (AABB). This is just a volume in space that knows how to
 * test intersections with a Ray (like a Hittable, but very fast and with no knowledge of what is
 * actually inside it).
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#boundingvolumehierarchies/axis-alignedboundingboxes(aabbs)">guide</a>
 */
public final class BoundingBox {

    private final Vector min;
    private final Vector max;

    public BoundingBox(Vector min, Vector max) {
        this.min = min.minWith(max);
        this.max = max.maxWith(min);
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }

    public boolean intersects(Ray ray, double tmin, double tmax) {
        for (int a=0; a<3; a++) {

            Vector ro = ray.origin();
            Vector rd = ray.direction();

            double id = 1.0f / rd.component(a);
            double t0 = (min.component(a) - ro.component(a)) * id;
            double t1 = (max.component(a) - ro.component(a)) * id;

            if (id < 0.0f) {
                double tmp = t1;
                t1 = t0;
                t0 = tmp;
            }

            tmin = Math.max(t0, tmin);
            tmax = Math.min(t1, tmax);
            if (tmax <= tmin) {
                return false;
            }
        }
        return true;
    }

    public static BoundingBox from(List<? extends Hittable> hittables) {
        Vector currMin = Vector.MAX;
        Vector currMax = Vector.MIN;
        for (Hittable hittable : hittables) {
            BoundingBox bounds = hittable.getBounds();
            if (bounds == null) {
                return null;
            }
            currMin = currMin.minWith(bounds.min);
            currMax = currMax.maxWith(bounds.max);
        }
        return new BoundingBox(currMin, currMax);
    }
}
