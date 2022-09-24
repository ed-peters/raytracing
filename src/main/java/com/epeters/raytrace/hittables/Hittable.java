package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Axis;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.utils.Vector;

/**
 * Generic interface for "something that can be hit by a {@link Ray}". This might be a
 * solid object or a more abstract bounding volume.
 */
public interface Hittable {

    Box getBounds();

    Hit intersect(Ray ray, double tmin, double tmax);

    default double pdfValue(Vector origin, Vector direction) {
        return 0.0;
    }

    default Vector directionTowards(Vector origin) {
        return Vector.ORIGIN;
    }

    default Hittable flip() {
        return new HittableFlip(this);
    }

    default Hittable translate(Vector offset) {
        return new HittableTranslation(this, offset);
    }

    default Hittable rotate(Axis axis, double degrees) {
        return new HittableRotation(this, axis, degrees);
    }
}
