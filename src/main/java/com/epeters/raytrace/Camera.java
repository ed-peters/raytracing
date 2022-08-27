package com.epeters.raytrace;

import com.epeters.raytrace.utils.Vector;

import static java.lang.Math.tan;
import static java.lang.Math.toRadians;
import static com.epeters.raytrace.utils.Utils.randomVectorInUnitDisc;

/**
 * Represents the camera in the scene. Knows its position and dimensional details,
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

    public Camera(SceneConfig config) {

        double viewportHeight = 2.0 * tan(toRadians(config.fieldOfView) / 2.0);
        double viewportWidth = config.aspectRatio * viewportHeight;
        double focalDistance = config.position.minus(config.target).length();

        this.w = config.position.minus(config.target).normalize();
        this.u = config.up.cross(w).normalize();
        this.v = w.cross(u);
        this.origin = config.position;
        this.horizontal = u.mul(viewportWidth).mul(focalDistance);
        this.vertical = v.mul(viewportHeight).mul(focalDistance);
        this.lowerLeft = origin
                .minus(horizontal.mul(0.5))
                .minus(vertical.mul(0.5))
                .minus(w.mul(focalDistance));
        this.lensRadius = config.aperture / 2.0;
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
