package com.epeters.raytrace.textures;

import com.epeters.raytrace.utils.TextureCoordinates;
import com.epeters.raytrace.utils.Vector;

import static java.lang.Math.sin;

/**
 * Implements a checkered texture that alernates between an "even" and "odd" texture.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#solidtextures/acheckertexture">guide</a>
 */
public class CheckeredTexture implements Texture {

    private final Texture even;
    private final Texture odd;

    public CheckeredTexture(Vector even, Vector odd) {
        this(Texture.solid(even), Texture.solid(odd));
    }

    public CheckeredTexture(Texture even, Texture odd) {
        this.even = even;
        this.odd = odd;
    }

    @Override
    public Vector calculateColor(Vector point, TextureCoordinates coords) {
        double sx = sin(10.0 * point.x());
        double sy = sin(10.0 * point.y());
        double sz = sin(10.0 * point.z());
        double sines = sx * sy * sz;
        return (sines < 0) ? odd.calculateColor(point, coords) : even.calculateColor(point, coords);
    }
}
