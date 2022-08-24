package com.epeters.raytrace.materials;

import com.epeters.raytrace.hittables.HitInfo;
import com.epeters.raytrace.Vector;

import static com.epeters.raytrace.Utils.WHITE;
import static com.epeters.raytrace.Utils.random;
import static com.epeters.raytrace.Utils.sqrt;
import static com.epeters.raytrace.Utils.square;

import static java.lang.Math.abs;

public class DialectricMaterial implements Material {

    private final double frontRatio;
    private final double backRatio;

    public DialectricMaterial(double indexOfRefraction) {
        this.frontRatio = 1.0 / indexOfRefraction;
        this.backRatio = indexOfRefraction;
    }

    @Override
    public void computeScatter(HitInfo hit) {
        double ratio = hit.isFront() ? frontRatio : backRatio;
        hit.setColor(WHITE);
        hit.setBounce(refract(hit.getRay().direction(), hit.getNormal(), ratio));
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
        Vector outPerp = incoming.plus(normal.mul(cos)).mul(ratio);
        Vector outPar = normal.mul(-sqrt(abs(1.0 - outPerp.square())));
        return outPerp.plus(outPar);
    }
}
