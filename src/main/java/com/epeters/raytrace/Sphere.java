package com.epeters.raytrace;

import static com.epeters.raytrace.Utils.sqrt;

/**
 * Implementation of {@link Hittable} logic for a simple sphere.
 */
public record Sphere(Vector center, double radius) implements Hittable {

    @Override
    public Hit hit(Ray ray, double tmin, double tmax) {

        Vector oc = ray.origin().minus(center);
        double a = ray.direction().square();
        double hb = oc.dot(ray.direction());
        double c = oc.square() - radius * radius;
        double d = hb * hb - a * c;
        if (d < 0.0) {
            return null;
        }

        double sd = sqrt(d);
        double t = (-hb - sd) / a;
        if (t < tmin || t > tmax) {
            t = (-hb + sd) / a;
            if (t < tmin || t > tmax) {
                return null;
            }
        }

        return Hit.from(ray, t, (p) -> p.minus(center).div(radius).normalize());
    }
}
