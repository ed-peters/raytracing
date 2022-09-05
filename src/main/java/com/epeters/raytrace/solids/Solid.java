package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.BoundingVolume;
import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.materials.IsotropicMaterial;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.materials.Material;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.solids.AxisAlignedRectangle.Type;

/**
 * Implementation of a solid object that knows how to be hit by a ray and then calculate
 * detailed information about the intersection.
 */
public abstract class Solid extends Hittable {

    private final Material material;

    protected Solid(BoundingBox bounds, Material material) {
        super(bounds);
        this.material = material;
    }

    public final Material getMaterial() {
        return material;
    }

    /**
     * @param ray a ray of interest
     * @param tmin minimum hit distance
     * @param tmax maximum hit distance
     * @return a {@link Hit} calculated using the underlying geometry (null if this is a miss)
     */
    @Override
    protected Hit computeHit(Ray ray, double tmin, double tmax) {
        double t = computeHitDistance(ray, tmin, tmax);
        if (Double.isNaN(t)) {
            return null;
        }
        return new Hit(ray, t, this::computeHitColor);
    }

    /**
     * Used by {@link #hit(Ray, double, double)} to determine hit distance
     * @return hit distance, or {@link Double#NaN} if there is no intersection
     */
    protected abstract double computeHitDistance(Ray ray, double tmin, double tmax);

    protected abstract HitColor computeHitColor(Vector point, Vector incoming);

    /**
     * @return a spherical solid with the supplied properties
     */
    public static Solid sphere(Vector center, double radius, Material material) {
        return new Sphere(material, center, radius);
    }

    /**
     * @return a rectangle aligned to the x and y axes
     */
    public static Solid rectXY(double x0, double y0, double x1, double y1, double z, Material material) {
        return new AxisAlignedRectangle(material, Type.XY, x0, y0, x1, y1, z);
    }

    /**
     * @return a rectangle aligned to the y and z axes
     */
    public static Solid rectYZ(double y0, double z0, double y1, double z1, double x, Material material) {
        return new AxisAlignedRectangle(material, Type.YZ, y0, z0, y1, z1, x);
    }

    /**
     * @return a rectangle aligned to the x and z axes
     */
    public static Solid rectXZ(double x0, double z0, double x1, double z1, double y, Material material) {
        return new AxisAlignedRectangle(material, Type.XZ, x0, z0, x1, z1, y);
    }

    /**
     * @return a box spanning the supplied points
     */
    public static Hittable box(Vector min, Vector max, Material material) {
        List<Solid> solids = new ArrayList<>();
        solids.add(rectXY(min.x(), min.y(), max.x(), max.y(), min.z(), material));
        solids.add(rectXY(min.x(), min.y(), max.x(), max.y(), max.z(), material));
        solids.add(rectXZ(min.x(), min.z(), max.x(), max.z(), min.y(), material));
        solids.add(rectXZ(min.x(), min.z(), max.x(), max.z(), max.y(), material));
        solids.add(rectYZ(min.y(), min.z(), max.y(), max.z(), min.x(), material));
        solids.add(rectYZ(min.y(), min.z(), max.y(), max.z(), max.x(), material));
        return BoundingVolume.from(solids);
    }

    public static Hittable fog(Hittable boundary, double density, Vector color) {
        return new ConstantMedium(new IsotropicMaterial(color), boundary, density);
    }
}
