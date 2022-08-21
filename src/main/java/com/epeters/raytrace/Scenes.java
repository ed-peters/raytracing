package com.epeters.raytrace;

import com.epeters.raytrace.material.Material;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.Vector.vec;

public class Scenes {

    public static List<Solid> threeBalls() {

        Material ground = Material.lambertian(0.8, 0.8, 0.0);
        Material left = Material.dialectric(1.5);
        Material center = Material.lambertian(0.1, 0.2, 0.5);
        Material right = Material.metal(0.8, 0.6, 0.2, 0.0);

        List<Solid> world = new ArrayList<>();
        world.add(Solid.sphere(vec(0.0, -100.5, -1.0), 100.0, ground));
        world.add(Solid.sphere(vec(-1.0, 0.0, -1.0), 0.5, left));
        world.add(Solid.sphere(vec(-1.0, 0.0, -1.0), -0.4, left));
        world.add(Solid.sphere(vec(0.0, 0.0, -1.0), 0.5, center));
        world.add(Solid.sphere(vec(1.0, 0.0, -1.0), 0.5, right));
        return world;
    }

    public static Tracer defaultThreeBalls() {
        return new Tracer(new CameraSettings(), threeBalls());
    }
}
