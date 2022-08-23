package com.epeters.raytrace.solids;

import com.epeters.raytrace.hitting.BoundingBox;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Implementation of {@link Solid} for spherical geometry that sits still.
 */
public final class Sphere extends Solid {

    private final Vector center;
    private final double radius;
    private final BoundingBox boundingBox;

    public Sphere(Material material, Vector center, double radius) {
        super(material);
        this.center = center;
        this.radius = radius;
        this.boundingBox = new BoundingBox(
                center.minus(vec(radius, radius, radius)),
                center.plus(vec(radius, radius, radius)));
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public Vector surfaceNormal(Vector point, double time) {
        return point.minus(center).div(radius).normalize();
    }

    @Override
    protected double hitDistance(Ray ray, double tmin, double tmax) {

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
