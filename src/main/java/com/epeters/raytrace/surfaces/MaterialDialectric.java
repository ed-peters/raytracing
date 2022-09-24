package com.epeters.raytrace.surfaces;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Scatter;
import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static java.lang.Math.abs;

public class MaterialDialectric implements Material {

    private final double backRatio;
    private final double frontRatio;

    public MaterialDialectric(double indexOfRefraction) {
        this.backRatio = indexOfRefraction;
        this.frontRatio = 1.0 / backRatio;
    }

    @Override
    public Scatter computeScatter(Ray ray, Hit hit) {
        double ratio = hit.front() ? frontRatio : backRatio;
        Vector bounce = refract(ray.direction(), hit.normal(), ratio);
        return Scatter.simpleBounce(Color.WHITE, bounce);
    }

    /**
     * Uses Snell's law for refraction, with Schlick's approximation for reflectance.
     * @return the result of refracting this ray through a surface with the given index of refraction.
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#dielectrics/snell'slaw">guide</a>
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#dielectrics/schlickapproximation">guide</a>
     */
    public static Vector refract(Vector incoming, Vector normal, double ratio) {

        double cos = Math.min(incoming.negate().dot(normal), 1.0);
        double sin = sqrt(1.0 - cos * cos);
        if (ratio * sin > 1.0) {
            return MaterialMetal.reflect(incoming, normal, 0.0);
        }

        double reflectance = (1.0 - ratio) / (1.0 + ratio);
        reflectance *= reflectance;
        reflectance += (1.0 - reflectance) * Math.pow(1.0 - cos, 5.0);
        if (reflectance > random(0.0, 1.0)) {
            return MaterialMetal.reflect(incoming, normal, 0.0);
        }

        Vector result = normal.mul(cos).plus(incoming).mul(ratio);
        double factor = -sqrt(abs(1.0 - result.square()));
        return result.plus(normal.mul(factor));
    }
}
