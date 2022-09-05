package com.epeters.raytrace.hittables;

import com.epeters.raytrace.utils.Mector;
import com.epeters.raytrace.utils.Vector;

import java.util.function.Function;

/**
 * Represents the details of coloring a particular point
 */
public record HitColor(Vector emission, Vector attenuation, Vector bounce) {

    public Vector combine(Function<Vector,Vector> tracer) {
        Mector c = new Mector();
        if (emission != null) {
            c.plus(emission);
        }
        if (attenuation != null && bounce != null) {
            c.plusTimes(attenuation, tracer.apply(bounce));
        }
        return c.toVector();
    }

    public static HitColor solid(Vector color) {
        return new HitColor(null, color, null);
    }

    public static HitColor bounced(Vector color, Vector bounce) {
        return new HitColor(null, color, bounce);
    }

    public static HitColor lit(Vector color) {
        return new HitColor(color, null, null);
    }
}
