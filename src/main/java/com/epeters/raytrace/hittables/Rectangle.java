package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.surfaces.MaterialParams;
import com.epeters.raytrace.utils.XYZPlane;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Implementation of a rectangular "slab" on the XY plane with a minimal thickness.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#rectanglesandlights/creatingrectangleobjects">guide</a>
 */
public final class Rectangle implements Hittable {

    private final Box bounds;
    private final Material material;
    private final XYZPlane type;
    private final double i0;
    private final double j0;
    private final double i1;
    private final double j1;
    private final double k;

    public Rectangle(Material material,
                     XYZPlane type,
                     double i0, double j0,
                     double i1, double j1,
                     double k) {

        double k0 = k - 0.0001;
        double k1 = k + 0.0001;

        this.bounds = switch (type) {
            case XY -> new Box(vec(i0, j0, k0), vec(i1, j1, k1));
            case YZ -> new Box(vec(k0, i0, j0), vec(k1, i1, j1));
            case XZ -> new Box(vec(i0, k0, j0), vec(i1, k1, j1));
        };
        this.material = material;
        this.type = type;
        this.i0 = min(i0, i1);
        this.i1 = max(i0, i1);
        this.j0 = min(j0, j1);
        this.j1 = max(j0, j1);
        this.k = k;
    }

    @Override
    public Box getBounds() {
        return bounds;
    }

    @Override
    public Hit intersect(Ray ray, double tmin, double tmax) {

        if (bounds.doesNotIntersect(ray, tmin, tmax)) {
            return null;
        }

        Vector ro = ray.origin();
        Vector rd = ray.direction();

        double t = (k - ro.component(type.k)) / rd.component(type.k);
        if (t < tmin || t > tmax) {
            return null;
        }

        double hi = ro.component(type.i) + t * ray.direction().component(type.i);
        double hj = ro.component(type.j) + t * ray.direction().component(type.j);
        if (hi < i0 || hi > i1 || hj < j0 || hj > j1) {
            return null;
        }

        return new Hit(ray, t, this::computeHitColor);
    }

    protected HitColor computeHitColor(Vector point, Vector incoming) {
        double u = (point.component(type.i) - i0) / (i1 - i0);
        double v = (point.component(type.j) - j0) / (j1 - j0);
        return material.computeHitColor(MaterialParams.from(point, incoming, type.normal, u, v));
    }

    private static Box makeBounds(XYZPlane type, double i0, double i1, double j0, double j1, double k) {
        double k0 = k - 0.0001;
        double k1 = k + 0.0001;
        return switch (type) {
            case XY -> new Box(vec(i0, j0, k0), vec(i1, j1, k1));
            case YZ -> new Box(vec(k0, i0, j0), vec(k1, i1, j1));
            case XZ -> new Box(vec(i0, k0, j0), vec(i1, k1, j1));
        };
    }
}
