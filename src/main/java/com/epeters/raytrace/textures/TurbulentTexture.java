package com.epeters.raytrace.textures;

import com.epeters.raytrace.utils.Perlin;
import com.epeters.raytrace.utils.Vector;

import static java.lang.Math.sin;

/**
 * Introduces a (slow) marble-like "turbulent" texture
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#perlinnoise/introducingturbulence">guide</a>
 */
public final class TurbulentTexture implements Texture {

    private final Perlin perlin;
    private final Vector base;
    private final double scale;
    private final int depth;

    public TurbulentTexture(Vector base, double scale, int depth) {
        this.perlin = new Perlin();
        this.base = base;
        this.scale = scale;
        this.depth = depth;
    }

    @Override
    public Vector calculateColor(Vector point, double u, double v) {
        double n = 0.5 * sin(scale * point.z() + 10.0 * perlin.turbulence(point, depth));
        return base.mul(n);
    }
}
