package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.Vector;
import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.HitInfo;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.materials.Material;

import java.util.function.Supplier;

/**
 * Implementation of a solid object that knows how to be hit by a ray and act appropriately.
 * This class mainly handles geometry, and delegates to a {@link Material} to handle scatter.
 */
public abstract class Solid implements Hittable {

    private final Material material;
    private final BoundingBox bounds;

    public Solid(Material material, BoundingBox bounds) {
        this.material = material;
        this.bounds = bounds;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public BoundingBox getBounds() {
        return bounds;
    }

    /**
     * @param ray a ray of interest
     * @param tmin minimum hit distance
     * @param tmax maximum hit distance
     * @return a {@link Hit} calculated using the underlying geometry (null if this is a miss)
     */
    @Override
    public Hit hit(Ray ray, double tmin, double tmax) {

        if (!getBounds().hit(ray, tmin, tmax)) {
            return null;
        }

        double t = computeHitDistance(ray, tmin, tmax);
        if (Double.isNaN(t)) {
            return null;
        }

        Supplier<HitInfo> supplier = () -> {
            Vector point = ray.at(t);
            Vector normal = computeSurfaceNormal(point);

            HitInfo info = new HitInfo(ray, this, point, normal);
            material.computeScatter(info);
            return info;
        };

        return new Hit(ray, t, supplier);
    }

    protected abstract double computeHitDistance(Ray ray, double tmin, double tmax);

    protected abstract Vector computeSurfaceNormal(Vector point);

    /**
     * @return a spherical solid with the supplied properties
     */
    public static Solid sphere(Vector center, double radius, Material material) {
        return new Sphere(material, center, radius);
    }
}
