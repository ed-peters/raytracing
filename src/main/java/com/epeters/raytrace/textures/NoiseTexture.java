package com.epeters.raytrace.textures;

import com.epeters.raytrace.utils.Perlin;
import com.epeters.raytrace.utils.Vector;

/**
 * Implements the basic (fast) "noisy" texture using Perlin noise generation with random vectors,
 * hermitian smoothing and all that jazz.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#perlinnoise/usingrandomvectorsonthelatticepoints">guide</a>
 */
public final class NoiseTexture implements Texture {

    private final Perlin perlin;
    private final Vector base;
    private final double scale;

    public NoiseTexture(Vector base, double scale) {
        this.perlin = new Perlin();
        this.base = base;
        this.scale = scale;
    }

    @Override
    public Vector calculateColor(Vector point, double u, double v) {
        double n = 0.5 * (1.0 - perlin.noise(point.mul(scale)));
        return base.mul(n);
    }
}
