package com.epeters.raytrace.solids;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.utils.TextureCoordinates;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Implementation of a rectangular "slab" on the XY plane with a minimal thickness.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#rectanglesandlights/creatingrectangleobjects">guide</a>
 */
public class XYRectangle extends Solid {

    public static final Vector NORMAL = vec(0.0, 0.0, 1.0);

    private final double x0;
    private final double y0;
    private final double x1;
    private final double y1;
    private final double k;

    public XYRectangle(Material material, double x0, double y0, double x1, double y1, double k) {
        super(material, new BoundingBox(vec(x0, y0, k-0.0001), vec(x1, y1, k+0.0001)));
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.k = k;
    }

    @Override
    protected double computeHitDistance(Ray ray, double tmin, double tmax) {

        Vector ro = ray.origin();
        Vector rd = ray.direction();

        double t = (k - ro.z()) / rd.z();
        if (t < tmin || t > tmax) {
            return Double.NaN;
        }

        double x = ro.x() + t * ray.direction().x();
        double y = ro.y() + t * ray.direction().y();
        if (x < x0 || x > x1 || y < y0 || y > y1) {
            return Double.NaN;
        }

        return t;
    }

    @Override
    protected Vector computeSurfaceNormal(Vector point) {
        return NORMAL;
    }

    @Override
    protected TextureCoordinates computeTextureCoordinates(Vector point, Vector normal) {
        double u = (point.x() - x0) / (x1 - x0);
        double v = (point.y() - y0) / (y1 - y0);
        return new TextureCoordinates(u, v);
    }
}
