package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Vector.MAX;
import static com.epeters.raytrace.utils.Vector.MIN;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

import static com.epeters.raytrace.utils.Vector.vec;

public final class RotatedHittable extends Hittable {

    public enum Axis {
        X,
        Y,
        Z
    }

    private final Hittable target;
    private final Axis axis;
    private final double sin;
    private final double cos;

    public RotatedHittable(Hittable target, Axis axis, double angle) {
        super(makeBounds(target.getBounds(), axis, angle));
        this.target = target;
        this.axis = axis;
        this.sin = sin(toRadians(angle));
        this.cos = cos(toRadians(angle));
    }

    @Override
    protected Hit computeHit(Ray originalRay, double tmin, double tmax) {

        // all of the rays and points supplied to this method will be in the outer coordinate
        // system; we need to apply the forward rotation in order to pass them along to the
        // target solid

        Ray transformedRay = transformRay(originalRay);
        Hit transformedHit = target.hit(transformedRay, tmin, tmax);
        if (transformedHit == null) {
            return null;
        }

        return new Hit(originalRay, transformedHit.t(), (point, incoming) -> {
            Vector transformedPoint = transformPoint(point);
            return transformedHit.color().apply(transformedPoint, transformedRay.direction());
        });
    }

    private Ray transformRay(Ray originalRay) {
        Vector ro = originalRay.origin();
        Vector to = ro.rotateY(sin, cos);
        Vector rd = originalRay.direction();
        Vector td = rd.rotateY(sin, cos);
        return new Ray(to, td);
    }

    private Vector transformPoint(Vector op) {
        Vector tp = op.rotateY(sin, cos);
        return tp;
    }

    private static BoundingBox makeBounds(BoundingBox original, Axis axis, double angle) {
        if (axis != Axis.Y) {
            throw new IllegalArgumentException(axis+"-axis rotation not supported");
        }

        double sin = sin(toRadians(angle));
        double cos = cos(toRadians(angle));

        Vector currMin = MAX;
        Vector currMax = MIN;

        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                for (int k=0; k<2; k++) {

                    double x = i * original.getMax().x() + (1-i) * original.getMin().x();
                    double y = j * original.getMax().y() + (1-j) * original.getMin().y();
                    double z = k * original.getMax().z() + (1-k) * original.getMin().z();

                    // the original solid reports its bounds in the inner coordinate system;
                    // we need to apply the reverse rotation to produce the coordinates in
                    // the outer coordinate system
                    double newX = cos * x + sin * z;
                    double newZ = cos * z - sin * x;

                    Vector tester = vec(newX, y, newZ);
                    currMin = currMin.minWith(tester);
                    currMax = currMax.maxWith(tester);
                }
            }
        }

        return new BoundingBox(currMin, currMax);
    }
}
