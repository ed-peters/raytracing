package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Axis;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.utils.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static com.epeters.raytrace.utils.Vector.vec;

public class HittableRotation implements Hittable {

    private final Hittable target;
    private final Box bounds;
    private final double sin;
    private final double cos;

    public HittableRotation(Hittable target, Axis axis, double degrees) {
        this.target = target;
        this.bounds = target.getBounds().rotate(axis, degrees);
        this.sin = sin(toRadians(degrees));
        this.cos = cos(toRadians(degrees));
    }

    @Override
    public Box getBounds() {
        return bounds;
    }

    public Hit intersect(Ray originalRay, double tmin, double tmax) {

        if (bounds.doesNotIntersect(originalRay, tmin, tmax)) {
            return null;
        }

        // rotate the incoming ray forwards and apply hit calculation
        Ray rotatedRay = rotate(originalRay, true);
        Hit rotatedHit = target.intersect(rotatedRay, tmin, tmax);
        if (rotatedHit == null) {
            return null;
        }

        // on the way back out, "unrotate" the hit point and the normal
        return new Hit(
                rotatedHit.t(),
                rotate(rotatedHit.point(), false),
                rotate(rotatedHit.normal(), false),
                rotatedHit.front(),
                rotatedHit.material(),
                rotatedHit.u(),
                rotatedHit.v());
    }

    protected Ray rotate(Ray originalRay, boolean forward) {
        Vector to = rotate(originalRay.origin(), forward);
        Vector td = rotate(originalRay.direction(), forward);
        return new Ray(to, td);
    }

    protected Vector rotate(Vector point, boolean forward) {
        double newX = cos * point.x() - (forward ? sin : -sin) * point.z();
        double newZ = cos * point.z() + (forward ? sin : -sin) * point.x();
        return vec(newX, point.y(), newZ);
    }
}
