package com.epeters.raytrace;

/**
 * Represents the camera in the scene. Knows its position and dimensional info,
 * and how to calculate a ray through the focal image.
 */
public class Camera {

    public static final float DEFAULT_ASPECT_RATIO = 16.0f / 9.0f;
    public static final float DEFAULT_VIEWPORT_HEIGHT = 2.0f;
    public static final float DEFAULT_FOCAL_LENGTH = 1.0f;

    public final float aspectRatio;
    public final Vector origin;
    public final Vector lowerLeft;
    public final Vector horizontal;
    public final Vector vertical;

    public Camera(Vector origin) {
        this(origin, DEFAULT_VIEWPORT_HEIGHT, DEFAULT_ASPECT_RATIO, DEFAULT_FOCAL_LENGTH);
    }

    public Camera(Vector origin, float viewportHeight, float aspectRatio, float focalLength) {

        float viewportWidth = aspectRatio * viewportHeight;
        Vector focalVector = new Vector(0.0f, 0.0f, focalLength);

        this.origin = origin;
        this.aspectRatio = aspectRatio;
        this.horizontal = new Vector(viewportWidth, 0.0f, 0.0f);
        this.vertical = new Vector(0.0f, viewportHeight, 0.0f);
        this.lowerLeft = origin
                .minus(horizontal.mul(0.5f))
                .minus(vertical.mul(0.5f))
                .minus(focalVector);
    }

    public Ray computeRay(float u, float v) {
        Vector direction = lowerLeft
                .plus(horizontal.mul(u))
                .plus(vertical.mul(v))
                .minus(origin);
        return new Ray(origin, direction);
    }
}
