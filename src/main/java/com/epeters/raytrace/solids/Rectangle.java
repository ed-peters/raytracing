package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.utils.XYZPlane;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Vector.vec;
import static com.epeters.raytrace.utils.Utils.random;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Implementation of a rectangular "slab" on a plane with a minimal thickness.
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

        Vector point = ray.at(t);
        double u = (point.component(type.i) - i0) / (i1 - i0);
        double v = (point.component(type.j) - j0) / (j1 - j0);

        return Hit.from(ray, t, point, type.normal, material, u, v);
    }

    @Override
    public double pdfValue(Vector origin, Vector direction) {

        Hit hit = intersect(new Ray(origin, direction), 0.001, Double.MAX_VALUE);
        if (hit == null) {
            return 0.0;
        }

        double area = (i1 - i0) * (j1 - j0);
        double dsq = hit.t() * hit.t() * direction.square();
        double cos = abs(direction.dot(hit.normal()) / direction.length());
        return dsq / (cos * area);
    }

    @Override
    public Vector directionTowards(Vector origin) {
        Vector randomPoint = type.fromIjk(random(i0, i1), random(j0, j1), k);
        return randomPoint.minus(origin).normalize();
    }
}
