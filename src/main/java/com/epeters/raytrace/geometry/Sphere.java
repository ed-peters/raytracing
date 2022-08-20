package com.epeters.raytrace.geometry;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.sqrt;

/**
 * Implementation of {@link Geometry} logic for a simple sphere.
 */
public record Sphere(Vector center, double radius) implements Geometry {

    @Override
    public Vector computeNormal(Vector point) {
        return point.minus(center).div(radius).normalize();
    }

    @Override
    public double hit(Ray ray, double tmin, double tmax) {

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
}
