package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.materials.MaterialParams;
import com.epeters.raytrace.utils.Vector;

import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Vector.vec;

public final class ConstantMedium extends Solid {

    public static final Vector NORMAL = vec(1.0, 0.0, 0.0);

    private final Hittable boundary;
    private final double negativeInverseDensity;

    public ConstantMedium(Material material, Hittable boundary, double density) {
        super(boundary.getBounds(), material);
        this.boundary = boundary;
        this.negativeInverseDensity = -1.0 / density;
    }

    @Override
    protected double computeHitDistance(Ray ray, double tmin, double tmax) {

        // project the ray across all of space and find the points where it intersects
        // with our boundary (we're assuming there are only two hits)
        Hit hit1 = boundary.hit(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Hit hit2 = hit1 == null ? null : boundary.hit(ray, hit1.t() + 0.0001, Double.POSITIVE_INFINITY);
        if (hit2 == null) {
            return Double.NaN;
        }

        // if the ray originates inside the boundary, or the boundary overlaps with
        // tmax, we'll truncate these values
        double r1t = max(tmin, hit1.t());
        double r2t = min(tmax, hit2.t());

        // using the two hit points, determine how far the ray travels inside the
        // boundary, and calculate a random fraction of that
//        Vector p1 = ray.at(r1t);
//        Vector p2 = ray.at(r2t);
//        double rayLength = p1.minus(p2).length();
        double rayLength = ray.direction().length();
        double distanceInsideBoundary = (r2t - r1t) * rayLength;
        double hitDistance = negativeInverseDensity * log(random());
        if (hitDistance > distanceInsideBoundary) {
            return Double.NaN;
        }

        return r1t + hitDistance / rayLength;
    }

    @Override
    protected HitColor computeHitColor(Vector point, Vector incoming) {
        MaterialParams params = MaterialParams.from(point, incoming, NORMAL, -1.0, -1.0);
        return getMaterial().computeHitColor(params);
    }
}
