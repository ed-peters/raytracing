package com.epeters.raytrace;

import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.renderer.Renderer;
import com.epeters.raytrace.solids.Rectangle;
import com.epeters.raytrace.solids.Solids;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.utils.Basis;
import com.epeters.raytrace.utils.Utils;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.utils.XYZPlane;

public class Main {

    public static void main2(String [] args) {

        Hittable r = Solids.rect(XYZPlane.XZ, -3, -3, 3, 3, -2, Material.norm());
        Vector v = r.directionTowards(Vector.ORIGIN);
        System.err.println(v);

    }

    public static void main(String [] args) {

        SceneConfig config = Scenes.cornellBoxWithObjects(false); // defaultThreeBalls();
        config.imageWidth = 600;
//        config.aspectRatio = 1.0;
        config.samplesPerPixel = 200;
        config.bouncesPerPixel = 50;

        int threads = Runtime.getRuntime().availableProcessors() - 1;
        String path = System.getProperty("user.home") + "/Desktop/trace.png";

        Tracer tracer = new Tracer(config);
        Renderer renderer = new Renderer(tracer, path, threads);
        Utils.time(renderer::render);
    }
}
