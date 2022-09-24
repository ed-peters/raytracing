package com.epeters.raytrace.hittables;

import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Vector;

/**
 * Represents the details of coloring a particular point
 */
public record Scatter(Type type, Color emission, Color attenuation, Vector direction, double pdf) {

    public enum Type {
        EMISSIVE,
        DIFFUSE,
        SPECULAR
    }

    public static Scatter emissive(Color color) {
        return new Scatter(Type.EMISSIVE, color, null, null, 0.0);
    }

    public static Scatter simpleBounce(Color albedo, Vector direction) {
        return new Scatter(Type.SPECULAR, Color.BLACK, albedo, direction, 0.0);
    }

    public static Scatter diffuse(Color color, Vector direction, double pdf) {
        return new Scatter(Type.DIFFUSE, Color.BLACK, color, direction, pdf);
    }
}
