package com.epeters.raytrace.rendering;

import com.epeters.raytrace.solids.Ray;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.randomVectorInUnitDisc;

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
    private final boolean motionBlur;

    /**
     * Builds camera properties from rendering configuration
     */
    public Camera(RendererConfig config) {

        // position and normalized vector space for camera based on its pose
        Vector w = config.position.minus(config.target).normalize();
        this.u = config.up.cross(w).normalize();
        this.v = w.cross(u);
        this.origin = config.position;

        // position and vectors describing the focal plane
        this.horizontal = u.mul(config.viewportWidth).mul(config.focalDistance);
        this.vertical = v.mul(config.viewportHeight).mul(config.focalDistance);
        this.lowerLeft = origin
                .minus(horizontal.mul(0.5))
                .minus(vertical.mul(0.5))
                .minus(w.mul(config.focalDistance));

        // lens radius for field of view calculations
        this.lensRadius = config.aperture / 2.0;

        // do we want to use motion blur?
        this.motionBlur = config.motionBlur;
    }

    /**
     * Computes a ray from the camera through the focal plane at the given coordinates
     */
    public Ray computeRay(double s, double t) {

        // vector originates from the camera and passes through the plane
        Vector origin = this.origin;
        Vector direction = lowerLeft
                .plus(horizontal.mul(s))
                .plus(vertical.mul(t))
                .minus(origin);

        // if we're applying field of view, we defract it slightly with the lens
        if (lensRadius > 0.0) {
            Vector rando = randomVectorInUnitDisc().mul(lensRadius);
            Vector offset = u.mul(rando.x()).plus(v.mul(rando.y()));
            origin = origin.plus(offset);
            direction = direction.minus(offset);
        }

        return new Ray(origin, direction, motionBlur ? random() : 0.0);
    }
}
