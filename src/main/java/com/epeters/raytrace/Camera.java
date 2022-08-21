package com.epeters.raytrace;

import static com.epeters.raytrace.Vector.vec;

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

    public Camera() {
        this(new CameraSettings());
    }

    public Camera(CameraSettings settings) {

        double viewportHeight = 2.0 * tan(toRadians(settings.fieldOfView) / 2.0);
        double viewportWidth = settings.aspectRatio * viewportHeight;
        Vector focalVector = vec(0.0, 0.0, settings.focalLength);

        this.origin = settings.origin;
        this.aspectRatio = settings.aspectRatio;
        this.horizontal = vec(viewportWidth, 0.0, 0.0);
        this.vertical = vec(0.0, viewportHeight, 0.0);
        this.lowerLeft = origin
                .minus(horizontal.mul(0.5))
                .minus(vertical.mul(0.5))
                .minus(focalVector);
    }

    public Ray computeRay(double u, double v) {
        Vector direction = lowerLeft
                .plus(horizontal.mul(u))
                .plus(vertical.mul(v))
                .minus(origin);
        return new Ray(origin, direction);
    }
}
