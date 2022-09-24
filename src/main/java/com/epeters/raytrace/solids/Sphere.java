package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.surfaces.Material;

import static com.epeters.raytrace.utils.Utils.dot;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;

/**
 * Concrete implementation of {@link Hittable} for a simple sphere.
 */
public final class Sphere implements Hittable {

    private final Box bounds;
    private final Material material;
    private final Vector center;
    private final double radius;

    public Sphere(Material material, Vector center, double radius) {
        Vector rvec = vec(abs(radius), abs(radius), abs(radius));
        this.bounds = new Box(center.minus(rvec), center.plus(rvec));
        this.material = material;
        this.center = center;
        this.radius = radius;
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

        double ocx = ro.x() - center.x();
        double ocy = ro.y() - center.y();
        double ocz = ro.z() - center.z();

        double a = dot(rd.x(), rd.y(), rd.z());
        double hb = dot(ocx, ocy, ocz, rd.x(), rd.y(), rd.z());
        double c = dot(ocx, ocy, ocz) - radius * radius;
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

        Vector point = ray.at(t);
        Vector normal = point.minus(center).mul(1.0 / radius).normalize();
        double theta = acos(-normal.y());
        double phi = atan2(-normal.z(), normal.x()) + PI;
        double u = phi / (2 * PI);
        double v = theta / PI;
        return Hit.from(ray, t, point, normal, material, u, v);
    }
}
