package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.utils.Vector;

import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Vector.vec;

public final class ConstantMedium implements Hittable {

    public static final Vector NORMAL = vec(1.0, 0.0, 0.0);

    private final Material material;
    private final Hittable boundary;
    private final double negativeInverseDensity;

    public ConstantMedium(Material material, Hittable boundary, double density) {
        this.material = material;
        this.boundary = boundary;
        this.negativeInverseDensity = -1.0 / density;
    }

    @Override
    public Box getBounds() {
        return boundary.getBounds();
    }

    @Override
    public Hit intersect(Ray ray, double tmin, double tmax) {

        // project the ray across all of space and find the points where it intersects
        // with our boundary (we're assuming there are only two hits)
        Hit hit1 = boundary.intersect(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Hit hit2 = hit1 == null ? null : boundary.intersect(ray, hit1.t() + 0.0001, Double.POSITIVE_INFINITY);
        if (hit2 == null) {
            return null;
        }

        // if the ray originates inside the boundary, or the boundary overlaps with
        // tmax, we'll truncate these values
        double r1t = max(tmin, hit1.t());
        double r2t = min(tmax, hit2.t());

        // using the two hit points, determine how far the ray travels inside the
        // boundary, and calculate a random fraction of that
        double rayLength = ray.direction().length();
        double distanceInsideBoundary = (r2t - r1t) * rayLength;
        double hitDistance = negativeInverseDensity * log(random());
        if (hitDistance > distanceInsideBoundary) {
            return null;
        }

        return Hit.from(ray, r1t + hitDistance / rayLength, NORMAL, material, -1.0, -1.0);
    }
}
