package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.materials.MaterialParams;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Implementation of a rectangular "slab" on the XY plane with a minimal thickness.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#rectanglesandlights/creatingrectangleobjects">guide</a>
 */
public final class AxisAlignedRectangle extends Solid {

    public enum Type {

        XY(0, 1, 2, vec(0.0, 0.0, 1.0)),
        YZ(1, 2, 0, vec(1.0, 0.0, 0.0)),
        XZ(0, 2, 1, vec(0.0, 1.0, 0.0));

        private final int i;
        private final int j;
        private final int k;
        private final Vector normal;

        private Type(int i, int j, int k, Vector normal) {
            this.i = i;
            this.j = j;
            this.k = k;
            this.normal = normal;
        }
    }

    private final Type type;
    private final double i0;
    private final double j0;
    private final double i1;
    private final double j1;
    private final double k;

    public AxisAlignedRectangle(Material material,
                                Type type,
                                double i0, double j0,
                                double i1, double j1,
                                double k) {
        super(makeBounds(type, i0, i1, j0, j1, k), material);
        this.type = type;
        this.i0 = min(i0, i1);
        this.i1 = max(i0, i1);
        this.j0 = min(j0, j1);
        this.j1 = max(j0, j1);
        this.k = k;
    }

    @Override
    protected double computeHitDistance(Ray ray, double tmin, double tmax) {

        Vector ro = ray.origin();
        Vector rd = ray.direction();

        double t = (k - ro.component(type.k)) / rd.component(type.k);
        if (t < tmin || t > tmax) {
            return Double.NaN;
        }

        double hi = ro.component(type.i) + t * ray.direction().component(type.i);
        double hj = ro.component(type.j) + t * ray.direction().component(type.j);
        if (hi < i0 || hi > i1 || hj < j0 || hj > j1) {
            return Double.NaN;
        }

        return t;
    }

    @Override
    protected HitColor computeHitColor(Vector point, Vector incoming) {
        double u = (point.component(type.i) - i0) / (i1 - i0);
        double v = (point.component(type.j) - j0) / (j1 - j0);
        return getMaterial().computeHitColor(MaterialParams.from(point, incoming, type.normal, u, v));
    }

    private static BoundingBox makeBounds(Type type, double i0, double i1, double j0, double j1, double k) {
        double k0 = k - 0.0001;
        double k1 = k + 0.0001;
        return switch (type) {
            case XY -> new BoundingBox(vec(i0, j0, k0), vec(i1, j1, k1));
            case YZ -> new BoundingBox(vec(k0, i0, j0), vec(k1, i1, j1));
            case XZ -> new BoundingBox(vec(i0, k0, j0), vec(i1, k1, j1));
        };
    }
}
