package com.epeters.raytrace.hitting;

import com.epeters.raytrace.solids.Ray;

import java.util.Comparator;

/**
 * Interface for a component that can be hit by a ray. This could either be notional
 * (e.g. {@link BoundingVolume}) or an actual physical object {@link com.epeters.raytrace.solids.Solid}.
 */
public interface Hittable {

    /**
     * @return the bounding box surrounding the component (this gets called a lot, so it's
     * expected to be very fast)
     */
    BoundingBox getBoundingBox();

    /**
     * @return a {@link Hit} if the given ray intersects the object; null otherwise
     */
    Hit computeHit(Ray ray, double tmin, double tmax);
}
