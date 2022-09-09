package com.epeters.raytrace.utils;

import com.epeters.raytrace.Ray;

import java.util.Arrays;
import java.util.List;

import static com.epeters.raytrace.utils.Vector.MAX;
import static com.epeters.raytrace.utils.Vector.MIN;
import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 * Represents an axis-aligned bounding box (AABB). This is just a volume in space that knows how to
 * test intersections with a Ray (like a Hittable, but very fast and with no knowledge of what is
 * actually inside it).
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#boundingvolumehierarchies/axis-alignedboundingboxes(aabbs)">guide</a>
 */
public record Box(Vector min, Vector max) {

    public Box(Vector min, Vector max) {
        this.min = min.minWith(max);
        this.max = max.maxWith(min);
    }

    public boolean doesNotIntersect(Ray ray, double tmin, double tmax) {
        for (Axis axis : Axis.values()) {

            Vector ro = ray.origin();
            Vector rd = ray.direction();

            double id = 1.0f / rd.component(axis);
            double t0 = (min.component(axis) - ro.component(axis)) * id;
            double t1 = (max.component(axis) - ro.component(axis)) * id;

            if (id < 0.0f) {
                double tmp = t1;
                t1 = t0;
                t0 = tmp;
            }

            tmin = Math.max(t0, tmin);
            tmax = Math.min(t1, tmax);
            if (tmax <= tmin) {
                return true;
            }
        }
        return false;
    }

    public Box translate(Vector offset) {
        if (offset == null) {
            return this;
        } else {
            return new Box(min.plus(offset), max.plus(offset));
        }
    }

    public Box rotate(Axis axis, double angle) {
        if (axis != Axis.Y) {
            throw new IllegalArgumentException(axis+"-axis rotation not supported");
        }

        double sin = sin(toRadians(angle));
        double cos = cos(toRadians(angle));

        Vector currMin = MAX;
        Vector currMax = MIN;

        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                for (int k=0; k<2; k++) {

                    double x = i * max.x() + (1-i) * min.x();
                    double y = j * max.y() + (1-j) * min.y();
                    double z = k * max.z() + (1-k) * min.z();

                    // the original solid reports its bounds in the inner coordinate system;
                    // we need to apply the reverse rotation to produce the coordinates in
                    // the outer coordinate system
                    double newX = cos * x + sin * z;
                    double newZ = cos * z - sin * x;

                    Vector tester = vec(newX, y, newZ);
                    currMin = currMin.minWith(tester);
                    currMax = currMax.maxWith(tester);
                }
            }
        }

        return new Box(currMin, currMax);
    }

    public static Box merge(Box left, Box right) {
        return merge(Arrays.asList(left, right));
    }

    public static Box merge(List<Box> boxes) {
        Vector currMin = Vector.MAX;
        Vector currMax = Vector.MIN;
        for (Box box : boxes) {
            if (box == null) {
                return null;
            }
            currMin = currMin.minWith(box.min);
            currMax = currMax.maxWith(box.max);
        }
        return new Box(currMin, currMax);
    }

    public static Box around(Vector... points) {
        Vector currMin = Vector.MAX;
        Vector currMax = Vector.MIN;
        for (Vector point : points) {
            currMin = currMin.minWith(point);
            currMax = currMax.maxWith(point);
        }
        return new Box(currMin, currMax);
    }
}
