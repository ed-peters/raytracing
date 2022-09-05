package com.epeters.raytrace.materials;

import com.epeters.raytrace.utils.Vector;

/**
 * Everything we need to know to calculate the color of a material
 *
 * @param point the point of intersection
 * @param incoming the direction of the incoming ray of light
 * @param normal the surface normal at the point
 * @param front was this a hit on the front or the back of the object?
 * @param u u-coordinate for texture mapping
 * @param v v-coordinate for texture mapping
 */
public record MaterialParams(Vector point, Vector incoming, Vector normal, boolean front, double u, double v) {

    public static boolean isBackFace(Vector incoming, Vector normal) {
        return incoming.dot(normal) > 0.0;
    }

    public static MaterialParams from(Vector point, Vector incoming, Vector normal, double u, double v) {
        boolean f = true;
        if (!incoming.isOpposite(normal)) {
            f = false;
            normal = normal.negate();
        }
        return new MaterialParams(point, incoming, normal, f, u, v);
    }
}
