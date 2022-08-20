package com.epeters.raytrace;

/**
 * Represents the camera in the scene. Knows its position and dimensional info,
 * and how to calculate a ray through the focal image.
 */
public class Camera {

    public static final double DEFAULT_ASPECT_RATIO = 16.0 / 9.0;
    public static final double DEFAULT_VIEWPORT_HEIGHT = 2.0;
    public static final double DEFAULT_FOCAL_LENGTH = 1.0;

    public final double aspectRatio;
    public final Vector origin;
    public final Vector lowerLeft;
    public final Vector horizontal;
    public final Vector vertical;

    public Camera(Vector origin) {
        this(origin, DEFAULT_VIEWPORT_HEIGHT, DEFAULT_ASPECT_RATIO, DEFAULT_FOCAL_LENGTH);
    }

    public Camera(Vector origin, double viewportHeight, double aspectRatio, double focalLength) {

        double viewportWidth = aspectRatio * viewportHeight;
        Vector focalVector = new Vector(0.0, 0.0, focalLength);

        this.origin = origin;
        this.aspectRatio = aspectRatio;
        this.horizontal = new Vector(viewportWidth, 0.0, 0.0);
        this.vertical = new Vector(0.0, viewportHeight, 0.0);
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
