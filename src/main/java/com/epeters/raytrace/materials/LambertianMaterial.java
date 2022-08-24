package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitInfo;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.randomUnitVector;

/**
 * Implements "true" Lambertian reflection for a matte material
 * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials/analternativediffuseformulation">guide</a>
 */
public class LambertianMaterial implements Material {

    private final Vector color;

    public LambertianMaterial(Vector color) {
        this.color = color;
    }

    @Override
    public void computeScatter(HitInfo hit) {
        hit.setColor(color);
        hit.setBounce(hit.getNormal().plus(randomUnitVector()));
    }
}
