package com.epeters.raytrace.solids;

import com.epeters.raytrace.hitting.Hit;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Utils.square;

import static java.lang.Math.abs;


/**
 * Implements the {@link Material} interface for "dialectrics" (e.g. glass).
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingInOneWeekend.html#dielectrics">guide</a>
 */
public class DialectricMaterial implements Material {

    private final double frontRatio;
    private final double backRatio;

    public DialectricMaterial(double indexOfRefraction) {
        this.frontRatio = 1.0 / indexOfRefraction;
        this.backRatio = indexOfRefraction;
    }

    @Override
    public Scatter computeScatter(Hit hit) {
        double ratio = hit.front() ? frontRatio : backRatio;
        return new Scatter(WHITE, refract(hit.ray().direction(), hit.normal(), ratio));
    }

    private Vector refract(Vector incoming, Vector normal, double ratio) {

        double cos = Math.min(incoming.negate().dot(normal), 1.0);
        double sin = sqrt(1.0 - cos * cos);
        boolean cannotRefract = ratio * sin > 1.0;

        // WTF if we negate the reflectance check, it looks a LOT more like glass
        if (cannotRefract || reflectance(cos, ratio) > random(0.0, 1.0)) {
            return incoming.reflect(normal, 0.0);
        } else {
            return refract(incoming, normal, cos, ratio);
        }
    }
    private double reflectance(double cos, double ratio) {
        double r0 = square((1.0 - ratio) / (1.0 + ratio));
        return r0 + (1.0 - r0) * Math.pow(1.0 - cos, 5.0);
    }

    private Vector refract(Vector incoming, Vector normal, double cos, double ratio) {
        Vector outPerp = incoming.plus(normal.mul(cos)).mul(ratio);
        Vector outPar = normal.mul(-sqrt(abs(1.0 - outPerp.square())));
        return outPerp.plus(outPar);
    }
}
