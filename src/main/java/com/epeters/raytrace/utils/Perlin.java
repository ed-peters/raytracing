package com.epeters.raytrace.utils;

import static com.epeters.raytrace.utils.Utils.randomInt;
import static com.epeters.raytrace.utils.Utils.randomUnitVector;
import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.abs;
import static java.lang.Math.floor;

/**
 * Generator for Perlin noise.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#perlinnoise">guide</a>
 */
public class Perlin {

    public static final int POINT_COUNT = 256;

    private final Vector [] randomVectors;
    private final int [] permutationX;
    private final int [] permutationY;
    private final int [] permutationZ;

    public Perlin() {
        this.randomVectors = generateVectors();
        this.permutationX = generatePermutation();
        this.permutationY = generatePermutation();
        this.permutationZ = generatePermutation();
    }

    public double noise(Vector point) {

        double u = point.x() - floor(point.x());
        double v = point.y() - floor(point.y());
        double w = point.z() - floor(point.z());
        u = u * u * (3.0 - 2.0 * u);
        v = v * v * (3.0 - 2.0 * v);
        w = w * w * (3.0 - 2.0 * w);

        int i = (int) floor(point.x());
        int j = (int) floor(point.y());
        int k = (int) floor(point.z());

        Vector [][][] c = new Vector[2][2][2];
        for (int di=0; di<2; di++) {
            for (int dj=0; dj<2; dj++) {
                for (int dk=0; dk<2; dk++) {
                    c[di][dj][dk] = randomVectors[
                        permutationX[(i + di) & 255] ^
                        permutationY[(j + dj) & 255] ^
                        permutationZ[(k + dk) & 255]
                    ];
                }
            }
        }

        return perlin_interp(c, u, v, w);
    }

    public double turbulence(Vector point, int depth) {
        double accum = 0.0;
        double weight = 1.0;

        for (int i=0; i<depth; i++) {
            accum += weight*noise(point);
            weight *= 0.5;
            point = point.mul(2.0);
        }

        return abs(accum);
    }

    private static double perlin_interp(Vector [][][] c, double u, double v, double w) {
        double uu = u*u*(3-2*u);
        double vv = v*v*(3-2*v);
        double ww = w*w*(3-2*w);
        double accum = 0.0;

        for (int i=0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    Vector weight_v = vec(u - i, v - j, w - k);
                    accum += (i * uu + (1 - i) * (1 - uu))
                            * (j * vv + (1 - j) * (1 - vv))
                            * (k * ww + (1 - k) * (1 - ww))
                            * c[i][j][k].dot(weight_v);
                }
            }
        }

        return accum;
    }

    private static int [] generatePermutation() {

        int [] data = new int[POINT_COUNT];
        for (int i=0; i<data.length; i++) {
            data[i] = i;
        }

        for (int i=POINT_COUNT-1; i > 0; i--) {
            int target = randomInt(i);
            int tmp = data[i];
            data[i] = data[target];
            data[target] = tmp;
        }

        return data;
    }

    private static Vector [] generateVectors() {
        Vector [] data = new Vector[POINT_COUNT];
        for (int i=0; i<data.length; i++) {
            data[i] = randomUnitVector();
        }
        return data;
    }
}
