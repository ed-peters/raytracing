package com.epeters.raytrace.textures;

import com.epeters.raytrace.utils.TextureCoordinates;
import com.epeters.raytrace.utils.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.epeters.raytrace.utils.Utils.clamp;
import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Implementation of an image-based texture.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#imagetexturemapping">guide</a>
 */
public class ImageTexture implements Texture {

    private final BufferedImage image;
    private final int width;
    private final int height;

    public ImageTexture(String path) {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public Vector calculateColor(Vector point, TextureCoordinates coords) {

        int i = (int) (clamp(coords.u(), 0.0, 1.0) * width);
        if (i > width) {
            i = width - 1;
        }

        int j = (int) (clamp((1.0 - coords.v()), 0.0, 1.0) * height);
        if (j > height) {
            j = height - 1;
        }

        double s = 1.0 / 255.0;
        int rgb = image.getRGB(i, j);
        double r = ((rgb >> 16) & 0xFF) * s;
        double g = ((rgb >> 8) & 0xFF) * s;
        double b = ((rgb >> 0) & 0xFF) * s;
        return vec(r, g, b);
    }
}
