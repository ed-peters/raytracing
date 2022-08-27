package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitDetails;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.clamp;

/**
 * Implements metal reflection with fuzzing
 * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#metal">guide</a>
 */
public class MetalMaterial implements Material {

    private final Vector color;
    private final double fuzz;

    public MetalMaterial(Vector color, double fuzz) {
        this.color = color;
        this.fuzz = clamp(fuzz, 0.0, 1.0);
    }

    @Override
    public Vector computeAttenuation(HitDetails hit) {
        return color;
    }

    @Override
    public Vector computeBounce(HitDetails hit) {
        return hit.ray().direction().reflect(hit.normal(), fuzz);
    }
}
