package com.epeters.raytrace;

import com.epeters.raytrace.utils.Vector;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Utils.time;

/**
 * Interface for a 2D grid of {@link Vector} instances representing colors.
 * Knows how to write itself out to image files.
 */
public class Image {

    public static final String PREAMBLE = """
            P3
            %d %d
            255
            """;

    public final int width;
    public final int height;
    private final Vector [] data;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new Vector[width * height];
        Arrays.fill(data, ORIGIN);
    }

    public int index(int x, int y) {
        return y * width + x;
    }

    public Vector get(int x, int y) {
        return data[index(x,y)];
    }

    public void set(int x, int y, Vector val) {
        data[index(x,y)] = val;
    }

    public void update(int x, int y, Function<Vector,Vector> func) {
        int idx = index(x, y);
        data[idx] = func.apply(data[idx]);
    }

    public void forEach(BiConsumer<Integer,Integer> func) {
        for (int y=height-1; y>=0; y--) {
            for (int x=0; x<width; x++) {
                func.accept(x, y);
            }
        }
    }
    public void writePpm(Writer writer) {
        try {
            writer.write(String.format(PREAMBLE, width, height));
            forEach((x, y) -> {
                int [] rgb = get(x,y).toRgb();
                try {
                    writer.write(String.format("%d %d %d\n", rgb[0], rgb[1], rgb[2]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writePpm(String path) {
        try {
            FileWriter writer = new FileWriter(path);
            writePpm(writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String [] args) {

        Image image = new Image(256, 256);
        time(() -> {
            image.forEach((x, y) -> {
                double r = x / 255.0;
                double g = y / 255.0;
                double b = 0.25;
                image.set(x, y, new Vector(r, g, b));
            });
            image.writePpm("/Users/ed.peters/Desktop/test.ppm");
        });
    }
}
