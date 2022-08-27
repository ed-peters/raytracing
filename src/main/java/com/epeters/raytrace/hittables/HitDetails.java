package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.solids.Solid;
import com.epeters.raytrace.utils.TextureCoordinates;
import com.epeters.raytrace.utils.Vector;

/**
 * Represents more detailed information about the intersection of a ray and a solid, including the point
 * of intersection, surface normal, color and bouncing information.
 */
public record HitDetails(
        Ray ray,
        Solid solid,
        Vector point,
        Vector normal,
        boolean front,
        TextureCoordinates textureCoords) {

    public static HitDetails from(Ray ray, Solid solid, Vector point, Vector normal, TextureCoordinates coords) {
        boolean front = ray.direction().dot(normal) < 0;
        if (!front) {
            normal = normal.negate().normalize();
        } else {
            normal = normal.normalize();
        }
        return new HitDetails(ray, solid, point, normal, front, coords);
    }
}
