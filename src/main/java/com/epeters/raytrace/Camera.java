package com.epeters.raytrace;

import static java.lang.Math.tan;
import static java.lang.Math.toRadians;
import static com.epeters.raytrace.Utils.randomVectorInUnitDisc;

/**
 * Represents the camera in the scene. Knows its position and dimensional info,
 * and how to calculate a ray through the focal image.
 */
public class Camera {

    private final double lensRadius;
    public final Vector origin;
    public final Vector lowerLeft;
    public final Vector horizontal;
    public final Vector vertical;
    private final Vector u;
    private final Vector v;
    private final Vector w;

    public Camera(CameraSettings settings) {

        double viewportHeight = 2.0 * tan(toRadians(settings.fieldOfView) / 2.0);
        double viewportWidth = settings.aspectRatio * viewportHeight;

        this.w = settings.position.minus(settings.target).normalize();
        this.u = settings.up.cross(w).normalize();
        this.v = w.cross(u);
        this.origin = settings.position;
        this.horizontal = u.mul(viewportWidth).mul(settings.focalDistance);
        this.vertical = v.mul(viewportHeight).mul(settings.focalDistance);
        this.lowerLeft = origin
                .minus(horizontal.mul(0.5))
                .minus(vertical.mul(0.5))
                .minus(w.mul(settings.focalDistance));
        this.lensRadius = settings.aperture / 2.0;
    }

    public Ray computeRay(double s, double t) {
        Vector origin = this.origin;
        Vector direction = lowerLeft
                .plus(horizontal.mul(s))
                .plus(vertical.mul(t))
                .minus(origin);
        if (lensRadius > 0.0) {
            Vector rando = randomVectorInUnitDisc().mul(lensRadius);
            Vector offset = u.mul(rando.x()).plus(v.mul(rando.y()));
            origin = origin.plus(offset);
            direction = direction.minus(offset);
        }
        return new Ray(origin, direction);
    }
}
