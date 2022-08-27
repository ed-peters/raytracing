package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.solids.Solid;

/**
 * Generic interface for "something that can be hit by a {@link Ray}". This might be a
 * {@link Solid} or a more abstract bounding volume.
 */
public interface Hittable {

    /** @return the bounding box for this hittable (this will be called a lot) */
    BoundingBox getBounds();

    /** @retyrn a new {@link Hit} if this ray hit something; null otherwise */
    Hit hit(Ray ray, double tmin, double tmax);
}
