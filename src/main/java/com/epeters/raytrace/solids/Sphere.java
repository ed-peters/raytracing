package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Mector;
import com.epeters.raytrace.utils.TextureCoordinates;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.materials.Material;

import static com.epeters.raytrace.utils.Utils.dot;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;

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

    @Override
    protected Vector computeSurfaceNormal(Vector point) {
        return new Mector().plus(point).minus(center).div(radius).normalize().toVector();
    }

    @Override
    protected TextureCoordinates computeTextureCoordinates(Vector point, Vector normal) {
        double theta = acos(-normal.y());
        double phi = atan2(-normal.z(), normal.x()) + PI;
        return new TextureCoordinates(phi / (2*PI), theta / PI);
    }

    public String toString() {
        return String.format("Sphere[material=%s, center=%s, radius=%.2f]",
                getMaterial().getClass().getSimpleName(),
                center, radius);
    }

    private static BoundingBox computeBounds(Vector center, double radius) {
        Vector rvec = vec(abs(radius), abs(radius), abs(radius));
        return new BoundingBox(center.minus(rvec), center.plus(rvec));
    }
}
