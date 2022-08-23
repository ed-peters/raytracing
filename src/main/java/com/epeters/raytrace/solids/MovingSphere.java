package com.epeters.raytrace.solids;

import com.epeters.raytrace.hitting.BoundingBox;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Implementation of {@link Solid} for spherical geometry that moves over the rendering time interval.
 */
public final class MovingSphere extends Solid {

    private final Vector centerStart;
    private final Vector centerInterval;
    private final double radius;
    private final BoundingBox boundingBox;

    /**
     * Creates a moving sphere
     *
     * @param material material for the sphere
     * @param centerStart center at start time
     * @param centerEnd center at end time
     * @param radius radius of the sphere
     */
    public MovingSphere(Material material, Vector centerStart, Vector centerEnd, double radius) {
        super(material);
        this.centerStart = centerStart;
        this.centerInterval = centerEnd.minus(centerStart);
        this.radius = radius;

        Vector radiusVec = vec(radius, radius, radius);
        BoundingBox box0 = new BoundingBox(center(0.0).minus(radiusVec), center(0.0).plus(radiusVec));
        BoundingBox box1 = new BoundingBox(center(1.0).minus(radiusVec), center(1.0).plus(radiusVec));
        this.boundingBox = BoundingBox.combine(box0, box1);
    }

    private Vector center(double time) {
        return centerStart.plus(centerInterval.mul(time));
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    /**
     * @return surface normal at the specified time (0.0 - 1.0)
     */
    @Override
    public Vector surfaceNormal(Vector point, double time) {
        return point.minus(center(time).div(radius)).normalize();
    }

    @Override
    protected double hitDistance(Ray ray, double tmin, double tmax) {

        Vector oc = ray.origin().minus(center(ray.time()));
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
