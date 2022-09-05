package com.epeters.raytrace.textures;

import com.epeters.raytrace.utils.Vector;

/**
 * Interface for a component that knows how to calculate surface color at a given point.
 */
public interface Texture {

    Vector calculateColor(Vector point, double u, double v);

    static Texture solid(Vector color) {
        return (p, u, v) -> color;
    }

    static Texture checker(Vector even, Vector odd) { return new CheckeredTexture(even, odd); }

    static Texture noise(Vector color, double scale) { return new NoiseTexture(color, scale); }

    static Texture turbulence(Vector color, double scale, int depth) { return new TurbulentTexture(color, scale, depth); }

    static Texture image(String path) { return new ImageTexture(path); }
}
