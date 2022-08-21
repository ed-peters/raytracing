package com.epeters.raytrace;

import com.epeters.raytrace.material.Material;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.Vector.vec;
import static com.epeters.raytrace.Utils.RED;
import static com.epeters.raytrace.Utils.BLUE;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Scenes {

    public static List<Solid> threeBalls() {

        Material ground = Material.lambertian(vec(0.8, 0.8, 0.0));
        Material left = Material.dialectric(1.5);
        Material center = Material.lambertian(vec(0.1, 0.2, 0.5));
        Material right = Material.metal(vec(0.8, 0.6, 0.2), 0.0);

        List<Solid> world = new ArrayList<>();
        world.add(Solid.sphere(vec(0.0, -100.5, -1.0), 100.0, ground));
        world.add(Solid.sphere(vec(-1.0, 0.0, -1.0), 0.5, left));
        world.add(Solid.sphere(vec(-1.0, 0.0, -1.0), -0.4, left));
        world.add(Solid.sphere(vec(0.0, 0.0, -1.0), 0.5, center));
        world.add(Solid.sphere(vec(1.0, 0.0, -1.0), 0.5, right));
        return world;
    }

    public static Tracer closeupSpheres() {

        Material blue = Material.lambertian(BLUE);
        Material red = Material.lambertian(RED);

        double r = cos(PI / 4.0);
        List<Solid> world = new ArrayList<>();
        world.add(Solid.sphere(vec(-r, 0.0, -1.0), r, blue));
        world.add(Solid.sphere(vec(r, 0.0, -1.0), r, red));

        CameraSettings settings = new CameraSettings();
        settings.fieldOfView = 90.0;
        settings.aspectRatio = 3.0 / 2.0;
        return new Tracer(settings, world);
    }

    public static Tracer farawayThreeBalls() {
        CameraSettings settings = new CameraSettings();
        settings.position = vec(-2.0,2.0,1.0);
        settings.target = vec(0.0,0.0,-1.0);
        settings.up = vec(0.0, 1.0, 0.0);
        settings.fieldOfView = 90.0;
        settings.aspectRatio = 3.0 / 2.0;
        return new Tracer(settings, threeBalls());

    }

    public static Tracer defaultThreeBalls() {
        return new Tracer(new CameraSettings(), threeBalls());
    }
}
