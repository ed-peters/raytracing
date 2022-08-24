package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitInfo;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.clamp;

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
    public void computeScatter(HitInfo hit) {
        Vector rd = hit.getRay().direction();
        hit.setColor(color);
        hit.setBounce(rd.reflect(hit.getNormal(), fuzz));
    }
}
