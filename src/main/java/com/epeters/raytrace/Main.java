package com.epeters.raytrace;

import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.renderer.Renderer;
import com.epeters.raytrace.solids.Solid;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.Vector.vec;
import static com.epeters.raytrace.materials.Material.dialectric;
import static com.epeters.raytrace.materials.Material.lambertian;
import static com.epeters.raytrace.materials.Material.metal;
import static com.epeters.raytrace.solids.Solid.sphere;

public class Main {

    public static void main(String [] args) {

        Material ground = lambertian(vec(0.8, 0.8, 0.0));
        Material left = dialectric(1.5);
        Material center = lambertian(vec(0.1, 0.2, 0.5));
        Material right = metal(vec(0.8, 0.6, 0.2), 0.0);

        List<Solid> world = new ArrayList<>();
        world.add(sphere(vec(0.0, -100.5, -1.0), 100.0, ground));
        world.add(sphere(vec(-1.0, 0.0, -1.0), 0.5, left));
        world.add(sphere(vec(-1.0, 0.0, -1.0), -0.4, left));
        world.add(sphere(vec(0.0, 0.0, -1.0), 0.5, center));
        world.add(sphere(vec(1.0, 0.0, -1.0), 0.5, right));

        TracerSettings settings = new TracerSettings();
        settings.useBoundingVolume = true;
        settings.aspectRatio = 3.0 / 2.0;
        settings.imageWidth = 600;
        settings.bouncesPerPixel = 10;
        settings.samplesPerPixel = 10;

        int threads = Runtime.getRuntime().availableProcessors() - 1;
        String path = System.getProperty("user.home") + "/Desktop/trace.png";

        Tracer tracer = new Tracer(settings, world);
        Renderer renderer = new Renderer(tracer, path, threads);
        Utils.time(() -> renderer.render());
    }

}
