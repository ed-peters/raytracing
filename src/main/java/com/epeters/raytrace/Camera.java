package com.epeters.raytrace;

import com.epeters.raytrace.utils.Mector;
import com.epeters.raytrace.utils.Vector;

import static java.lang.Math.tan;
import static java.lang.Math.toRadians;
import static com.epeters.raytrace.utils.Utils.randomVectorInUnitDisc;

/**
 * Represents the camera in the scene. Knows its position and dimensional details,
 * and how to calculate a ray through the focal image.
 */
public final class Camera {

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
        Mector o = new Mector(origin);
        Mector d = new Mector(lowerLeft)
                .plusTimes(horizontal, s)
                .plusTimes(vertical, t)
                .minus(origin);
        if (lensRadius > 0.0) {
            Vector rando = randomVectorInUnitDisc().mul(lensRadius);
            Vector f = new Mector()
                    .plusTimes(u, rando.x())
                    .plusTimes(v, rando.y())
                    .toVector();
            o.plus(f);
            d.minus(f);
        }
        return new Ray(o.toVector(), d.toVector());
    }
}
