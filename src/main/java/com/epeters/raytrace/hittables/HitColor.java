package com.epeters.raytrace.hittables;

import com.epeters.raytrace.utils.Vector;

/**
 * Represents the details of coloring a particular point
 */
public record HitColor(Vector emission, Vector attenuation, Vector bounce) {

    public static HitColor bounced(Vector color, Vector bounce) {
        return new HitColor(null, color, bounce);
    }

    public static HitColor lit(Vector color) {
        return new HitColor(color, null, null);
    }
}
