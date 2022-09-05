package com.epeters.raytrace.materials;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.utils.Mector;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Utils.square;

import static java.lang.Math.abs;

/**
 * Implementation of a material that allows light to pass through it, possibly refracting it along
 * the way. This can be used for e.g. glass and water.
 */
public final class ClearMaterial implements Material {

    private final double frontRatio;
    private final double backRatio;

    public ClearMaterial(double indexOfRefraction) {
        this.frontRatio = 1.0 / indexOfRefraction;
        this.backRatio = indexOfRefraction;
    }

    @Override
    public HitColor computeHitColor(MaterialParams params) {
        double ratio = params.front() ? frontRatio : backRatio;
        Vector bounce = refract(params.incoming(), params.normal(), ratio);
        return HitColor.bounced(WHITE, bounce);
    }

    private Vector refract(Vector incoming, Vector normal, double ratio) {

        double cos = Math.min(incoming.negate().dot(normal), 1.0);
        double sin = sqrt(1.0 - cos * cos);
        boolean cannotRefract = ratio * sin > 1.0;

        // WTF if we negate the reflectance check, it looks a LOT more like glass
        Vector result = null;
        if (cannotRefract || reflectance(cos, ratio) > random(0.0, 1.0)) {
            result = incoming.reflect(normal, 0.0);
        } else {
            result = refract(incoming, normal, cos, ratio);
        }
        return result;
    }

    /**
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#dielectrics/schlickapproximation">guide</a>
     * @return Schlick's approximation for reflectance
     */
    private double reflectance(double cos, double ratio) {
        double r0 = square((1.0 - ratio) / (1.0 + ratio));
        return r0 + (1.0 - r0) * Math.pow(1.0 - cos, 5.0);
    }

    /**
     * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#dielectrics/snell'slaw">guide</a>
     * @return refraction based on Snell's law
     */
    private Vector refract(Vector incoming, Vector normal, double cos, double ratio) {
        Mector result = new Mector(normal).mul(cos).plus(incoming).mul(ratio);
        double factor = -sqrt(abs(1.0 - result.square()));
        return result.plusTimes(normal, factor).toVector();
    }
}
