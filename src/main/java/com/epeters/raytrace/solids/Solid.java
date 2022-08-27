package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.TextureCoordinates;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.HitDetails;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.materials.Material;

/**
 * Implementation of a solid object that knows how to be hit by a ray and then calculate
 * detailed information about the intersection.
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

        return new Hit(ray, t, this);
    }

    /**
     * Used by {@link #hit(Ray, double, double)} to determine hit distance
     * @return hit distance, or {@link Double#NaN} if there is no intersection
     */
    protected abstract double computeHitDistance(Ray ray, double tmin, double tmax);

    /**
     * @param hit the information about a hit
     * @return detailed information about the hit
     */
    public HitDetails computeHitDetails(Hit hit) {
        Vector point = hit.ray().at(hit.t());
        Vector normal = computeSurfaceNormal(point);
        TextureCoordinates coords = computeTextureCoordinates(point, normal);
        return HitDetails.from(hit.ray(), this, point, normal, coords);
    }

    /**
     * @return the outward surface normal at the supplied point
     */
    protected abstract Vector computeSurfaceNormal(Vector point);

    /**
     * @return the UV texture coordinates at the supplied point
     */
    protected abstract TextureCoordinates computeTextureCoordinates(Vector point, Vector normal);

    /**
     * @return a spherical solid with the supplied properties
     */
    public static Solid sphere(Vector center, double radius, Material material) {
        return new Sphere(material, center, radius);
    }

    /**
     * @return a rectangle
     */
    public static Solid rect(double x0, double y0, double x1, double y1, double z, Material material) {
        return new XYRectangle(material, x0, y0, x1, y1, z);
    }
}
