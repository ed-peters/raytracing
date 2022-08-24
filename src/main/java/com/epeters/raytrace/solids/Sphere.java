package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.Vector;
import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.materials.Material;

import static com.epeters.raytrace.Utils.sqrt;
import static com.epeters.raytrace.Vector.vec;
import static java.lang.Math.abs;

/**
 * Concrete implementation of {@link Solid} for a simple sphere.
 */
public class Sphere extends Solid {

    private final Vector center;
    private final double radius;

    public Sphere(Material material, Vector center, double radius) {
        super(material, computeBounds(center, radius));
        this.center = center;
        this.radius = radius;
    }

    @Override
    protected double computeHitDistance(Ray ray, double tmin, double tmax) {

        Vector oc = ray.origin().minus(center);
        double a = ray.direction().square();
        double hb = oc.dot(ray.direction());
        double c = oc.square() - radius * radius;
        double d = hb * hb - a * c;
        if (d < 0.0) {
            return Double.NaN;
        }

        double sd = sqrt(d);
        double t = (-hb - sd) / a;
        if (t < tmin || t > tmax) {
            t = (-hb + sd) / a;
            if (t < tmin || t > tmax) {
                return Double.NaN;
            }
        }

        return t;
    }

    public String toString() {
        return String.format("Sphere[material=%s, center=%s, radius=%.2f]",
                getMaterial().getClass().getSimpleName(),
                center, radius);
    }

    @Override
    protected Vector computeSurfaceNormal(Vector point) {
        return point.minus(center).div(radius).normalize();
    }

    private static BoundingBox computeBounds(Vector center, double radius) {
        Vector rvec = vec(abs(radius), abs(radius), abs(radius));
        return new BoundingBox(center.minus(rvec), center.plus(rvec));
    }
}
