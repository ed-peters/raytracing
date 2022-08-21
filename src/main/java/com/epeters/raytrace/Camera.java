package com.epeters.raytrace;

import static java.lang.Math.tan;
import static java.lang.Math.toRadians;

/**
 * Represents the camera in the scene. Knows its position and dimensional info,
 * and how to calculate a ray through the focal image.
 */
public class Camera {

    public final double aspectRatio;
    public final Vector origin;
    public final Vector lowerLeft;
    public final Vector horizontal;
    public final Vector vertical;

    public Camera(CameraSettings settings) {

        double viewportHeight = 2.0 * tan(toRadians(settings.fieldOfView) / 2.0);
        double viewportWidth = settings.aspectRatio * viewportHeight;

        Vector w = settings.position.minus(settings.target).normalize();
        Vector u = settings.up.cross(w).normalize();
        Vector v = w.cross(u);

        this.origin = settings.position;
        this.aspectRatio = settings.aspectRatio;
        this.horizontal = u.mul(viewportWidth);
        this.vertical = v.mul(viewportHeight);
        this.lowerLeft = origin
                .minus(horizontal.mul(0.5))
                .minus(vertical.mul(0.5))
                .minus(w);
    }

    public Ray computeRay(double u, double v) {
        Vector direction = lowerLeft
                .plus(horizontal.mul(u))
                .plus(vertical.mul(v))
                .minus(origin);
        return new Ray(origin, direction);
    }
}
