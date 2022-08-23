package com.epeters.raytrace.solids;

import com.epeters.raytrace.hitting.BoundingBox;
import com.epeters.raytrace.hitting.Hit;
import com.epeters.raytrace.hitting.Hittable;
import com.epeters.raytrace.utils.Vector;

/**
 * Implementation of {@link Hittable} that represents a solid object.
 */
public abstract class Solid implements Hittable {

    private final Material material;

    protected Solid(Material material) {
        this.material = material;
    }

    /**
     * All subclasses must know how to compute their bounding box
     */
    public abstract BoundingBox getBoundingBox();

    /**
     * Computing a hit breaks down into a two-method protocol for subclasses.
     * The need to determine the t-value of the hit, and the surface normal
     * at the point of intersection.
     */
    public Hit computeHit(Ray ray, double tmin, double tmax) {

        double t = hitDistance(ray, tmin, tmax);
        if (Double.isNaN(t)) {
            return null;
        }

        Vector point = ray.at(t);
        Vector normal = surfaceNormal(point, ray.time());
        boolean front = ray.direction().isOpposite(normal);
        return new Hit(ray, this, t, point, front ? normal : normal.negate(), front);
    }

    /** Step 1 of {@link #computeHit(Ray, double, double)} */
    protected abstract double hitDistance(Ray ray, double tmin, double tmax);

    /* Step 2 of {@link #hit(Ray, double, double)} */
    protected abstract Vector surfaceNormal(Vector point, double time);

    /**
     * Solids use their material to determine how light scatters when
     * it hits them.
     */
    public final Scatter scatter(Hit hit) {
        return material == null ? null : material.computeScatter(hit);
    }

    /**
     * @return a spherical solid with the supplied properties
     */
    public static Solid sphere(Material material, Vector center, double radius) {
        return new Sphere(material, center, radius);
    }

    /**
     * @return a moving spherical solid with the supplied properties
     */
    public static Solid movingSphere(Material material, Vector centerStart, Vector centerEnd, double radius) {
        return new MovingSphere(material, centerStart, centerEnd, radius);
    }
}
