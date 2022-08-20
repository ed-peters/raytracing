package com.epeters.raytrace;

import static com.epeters.raytrace.Utils.sqrt;

/**
 * Implementation of {@link Hittable} logic for a simple sphere.
 */
public record Sphere(Vector center, float radius) implements Hittable {

    @Override
    public Hit hit(Ray ray, float tmin, float tmax) {

        Vector oc = ray.origin().minus(center);
        float a = ray.direction().square();
        float hb = oc.dot(ray.direction());
        float c = oc.square() - radius * radius;
        float d = hb * hb - a * c;
        if (d < 0.0f) {
            return null;
        }

        float sd = sqrt(d);
        float t = (-hb - sd) / a;
        if (t < tmin || t > tmax) {
            t = (-hb + sd) / a;
            if (t < tmin || t > tmax) {
                return null;
            }
        }

        return Hit.from(ray, t, (p) -> p.minus(center).div(radius).normalize());
    }
}
