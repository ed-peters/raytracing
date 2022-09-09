package com.epeters.raytrace.surfaces;

import com.epeters.raytrace.utils.Perlin;
import com.epeters.raytrace.utils.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.epeters.raytrace.utils.Utils.clamp;
import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.sin;

/**
 * Interface for a component that knows how to calculate surface color at a given point, plus
 * various implementations for common use cases.
 */
public interface Texture {

    /**
     * The main method for all textures to implement
     */
    Vector calculateColor(Vector point, double u, double v);

    /**
     * Uniform color at any point
     */
    static Texture solid(Vector color) {
        return (p, u, v) -> color;
    }

    /**
     * Alternating squares of color
     */
    static Texture checker(Vector even, Vector odd) {
        return (p, u, v) -> {
            double sx = sin(10.0 * p.x());
            double sy = sin(10.0 * p.y());
            double sz = sin(10.0 * p.z());
            double sines = sx * sy * sz;
            return (sines < 0) ? odd : even;
        };
    }

    /**
     * Perlin noise
     */
    static Texture noise(Vector color, double scale) {
        Perlin perlin = new Perlin();
        return (p, u, v) -> {
            double n = 0.5 * (1.0 - perlin.noise(p.mul(scale)));
            return color.mul(n);
        };
    }

    /**
     * Marble-like texture
     */
    static Texture turbulence(Vector color, double scale, int depth) {
        Perlin perlin = new Perlin();
        return (p, u, v) -> {
            double n = 0.5 * sin(scale * p.z() + 10.0 * perlin.turbulence(p, depth));
            return color.mul(n);
        };
    }

    /**
     * Image-mapped texture
     */
    static Texture image(String path) {
        try {
            final BufferedImage image = ImageIO.read(Texture.class.getResourceAsStream(path));
            final int width = image.getWidth();
            final int height = image.getHeight();
            return (p, u, v) -> {

                int i = (int) (clamp(u, 0.0, 1.0) * width);
                if (i > width) {
                    i = width - 1;
                }

                int j = (int) (clamp((1.0 - v), 0.0, 1.0) * height);
                if (j > height) {
                    j = height - 1;
                }

                double s = 1.0 / 255.0;
                int rgb = image.getRGB(i, j);
                double r = ((rgb >> 16) & 0xFF) * s;
                double g = ((rgb >> 8) & 0xFF) * s;
                double b = (rgb & 0xFF) * s;
                return vec(r, g, b);
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
