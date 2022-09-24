package com.epeters.raytrace.surfaces;

import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Perlin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.epeters.raytrace.utils.Utils.clamp;
import static java.lang.Math.sin;

/**
 * Interface for a component that knows how to calculate surface color at a given point, plus
 * various implementations for common use cases.
 */
public interface Texture {

    /**
     * The main method for all textures to implement
     */
    Color calculateColor(Hit hit);

    /**
     * Uniform color at any point
     */
    static Texture solid(Color color) {
        return (hit) -> color;
    }

    /**
     * Alternating squares of color
     */
    static Texture checker(Color even, Color odd) {
        return (hit) -> {
            double sx = sin(10.0 * hit.point().x());
            double sy = sin(10.0 * hit.point().y());
            double sz = sin(10.0 * hit.point().z());
            double sines = sx * sy * sz;
            return (sines < 0) ? odd : even;
        };
    }

    /**
     * Perlin noise
     */
    static Texture noise(Color color, double scale) {
        Perlin perlin = new Perlin();
        return (hit) -> {
            double n = 0.5 * (1.0 - perlin.noise(hit.point().mul(scale)));
            return color.mul(n);
        };
    }

    /**
     * Marble-like texture
     */
    static Texture turbulence(Color color, double scale, int depth) {
        Perlin perlin = new Perlin();
        return (hit) -> {
            double n = 0.5 * sin(scale * hit.point().z() + 10.0 * perlin.turbulence(hit.point(), depth));
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
            return (hit) -> {

                int i = (int) (clamp(hit.u(), 0.0, 1.0) * width);
                if (i > width) {
                    i = width - 1;
                }

                int j = (int) (clamp((1.0 - hit.v()), 0.0, 1.0) * height);
                if (j > height) {
                    j = height - 1;
                }

                double s = 1.0 / 255.0;
                int rgb = image.getRGB(i, j);
                double r = ((rgb >> 16) & 0xFF) * s;
                double g = ((rgb >> 8) & 0xFF) * s;
                double b = (rgb & 0xFF) * s;
                return Color.color(r, g, b);
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
