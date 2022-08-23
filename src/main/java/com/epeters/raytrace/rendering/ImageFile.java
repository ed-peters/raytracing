package com.epeters.raytrace.rendering;

import com.epeters.raytrace.utils.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.epeters.raytrace.utils.Utils.scaleInt;

/**
 * Knows how to write pixels to an image file in PNG format.
 */
public class ImageFile {

    private final File file;
    private final BufferedImage image;
    private final int imageWidth;
    private int currentX;
    private int currentY;

    public ImageFile(String path, int width, int height) {
        this.file = new File(path);
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.imageWidth = width;
        this.currentX = 0;
        this.currentY = 0;
    }

    public void write(Vector pixel) {

        int r = scaleInt(pixel.x(), 255);
        int g = scaleInt(pixel.y(), 255);
        int b = scaleInt(pixel.z(), 255);

        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        image.setRGB(currentX, currentY, 0xFF000000 | r | g | b);

        currentX += 1;
        if (currentX == imageWidth) {
            currentX = 0;
            currentY += 1;
        }
    }

    public void close() {
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
