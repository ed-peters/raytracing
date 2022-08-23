package com.epeters.raytrace;

import com.epeters.raytrace.hitting.BoundingBox;
import com.epeters.raytrace.hitting.BoundingVolume;
import com.epeters.raytrace.hitting.Hittable;
import com.epeters.raytrace.rendering.Renderer;
import com.epeters.raytrace.rendering.RendererConfig;
import com.epeters.raytrace.rendering.ImageFile;
import com.epeters.raytrace.solids.Ray;
import com.epeters.raytrace.solids.Solid;
import com.epeters.raytrace.utils.Utils;
import com.epeters.raytrace.utils.Vector;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static final String FILE = "/Users/ed.peters/Desktop/trace.png";

    public static void main(String [] args) throws Exception {

        RendererConfig config = Scenes.randomWorld(false);
        config.imageWidth = 600;
        config.samplesPerPixel = 10;
        config.bouncesPerPixel = 10;
        config.threads = 7;
        config.aperture = 0.0;
        config.boundingVolume = true;
        config.init();

        ImageFile file = new ImageFile(FILE, config.imageWidth, config.imageHeight);
        List<Vector[]> data = Utils.time(() -> new Renderer(config).render());
        data.stream()
            .flatMap(row -> Arrays.stream(row))
            .forEach(file::write);
        file.close();
    }

    public static void main2(String [] args) {

        BoundingBox box = new BoundingBox(
                Vector.vec(-1.0, -1.0, -5.0),
                Vector.vec(1.0, 1.0, -5.0));
        Ray ray = Ray.from(0.0000001, 0.000001, 1.0);
        System.err.println(box.hit(ray, 0.000001, Double.MAX_VALUE));
    }
}
