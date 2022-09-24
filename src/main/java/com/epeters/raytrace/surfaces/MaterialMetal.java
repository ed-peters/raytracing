package com.epeters.raytrace.surfaces;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Scatter;
import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.randomUnitVector;

public class MaterialMetal implements Material {

    private final Texture texture;
    private final double fuzz;

    public MaterialMetal(Texture texture, double fuzz) {
        this.texture = texture;
        this.fuzz = fuzz;
    }

    @Override
    public Scatter computeScatter(Ray ray, Hit hit) {
        Color color = texture.calculateColor(hit);
        Vector bounce = reflect(ray.direction(), hit.normal(), fuzz);
        return Scatter.simpleBounce(color, bounce);
    }

    /**
     * @return the result of reflecting this vector against the supplied normal, with optional fuzz
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#metal/mirroredlightreflection">doc</a>
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#metal/fuzzyreflection">doc</a>
     */
    public static Vector reflect(Vector incoming, Vector normal, double fuzz) {
        double d = 2.0 * incoming.dot(normal);
        Vector v = incoming.plus(normal.mul(-d));
        if (fuzz > 0.0) {
            v = v.plus(randomUnitVector().mul(fuzz));
        }
        return v;
    }
}
