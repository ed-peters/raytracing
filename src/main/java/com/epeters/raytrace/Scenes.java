package com.epeters.raytrace;

import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.solids.Solid;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.Utils.MID_GRAY;
import static com.epeters.raytrace.Utils.random;
import static com.epeters.raytrace.Utils.randomVectorInUnitCube;
import static com.epeters.raytrace.Vector.ORIGIN;
import static com.epeters.raytrace.Vector.vec;
import static com.epeters.raytrace.Utils.RED;
import static com.epeters.raytrace.Utils.BLUE;
import static com.epeters.raytrace.materials.Material.lambertian;
import static com.epeters.raytrace.materials.Material.metal;
import static com.epeters.raytrace.materials.Material.dialectric;
import static com.epeters.raytrace.solids.Solid.sphere;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Scenes {

    public static List<Solid> threeBalls() {

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
        return world;
    }

    public static Tracer closeupSpheres() {

        Material blue = lambertian(BLUE);
        Material red = lambertian(RED);

        double r = cos(PI / 4.0);
        List<Solid> world = new ArrayList<>();
        world.add(sphere(vec(-r, 0.0, -1.0), r, blue));
        world.add(sphere(vec(r, 0.0, -1.0), r, red));

        TracerSettings settings = new TracerSettings();
        settings.fieldOfView = 90.0;
        settings.aspectRatio = 3.0 / 2.0;
        return new Tracer(settings, world);
    }

    public static Tracer farawayThreeBalls() {
        TracerSettings settings = new TracerSettings();
        settings.position = vec(-2.0,2.0,1.0);
        settings.target = vec(0.0,0.0,-1.0);
        settings.up = vec(0.0, 1.0, 0.0);
        settings.fieldOfView = 90.0;
        settings.aspectRatio = 3.0 / 2.0;
        return new Tracer(settings, threeBalls());
    }

    public static Tracer closeupThreeBalls() {
        TracerSettings settings = new TracerSettings();
        settings.position = vec(-2.0,2.0,1.0);
        settings.target = vec(0.0,0.0,-1.0);
        settings.up = vec(0.0, 1.0, 0.0);
        settings.fieldOfView = 20;
        settings.aspectRatio = 3.0 / 2.0;
        return new Tracer(settings, threeBalls());
    }

    public static Tracer fuzzyThreeBalls() {
        TracerSettings settings = new TracerSettings();
        settings.position = vec(3.0, 3.0, 2.0);
        settings.target = vec(0.0, 0.0, -1.0);
        settings.up = vec(0.0, 1.0, 0.0);
        settings.focalDistance = settings.position.minus(settings.target).length();
        settings.aperture = 2.0;
        settings.fieldOfView = 20.0;
        return new Tracer(settings, threeBalls());
    }

    public static Vector randomColor() {
        return randomVectorInUnitCube().mul(randomVectorInUnitCube());
    }

    public static Tracer randomWorld() {

        List<Solid> world = new ArrayList<>();
        world.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, lambertian(MID_GRAY)));

        Vector comp = vec(4.0, 0.2, 0.0);

        for (int a=-11; a<11; a++) {
            for (int b=-11; b<11; b++) {

                Vector center = vec(a + 0.9 * random(), 0.2, b + 0.9 * random());
                if (center.minus(comp).length() > 0.9) {
                    double material = random();
                    if (material < 0.8) {
                        Vector color = randomColor();
                        world.add(sphere(center, 0.2, lambertian(color)));
                    } else if (material < 0.95) {
                        Vector color = randomColor();
                        double fuzz = random(0.0, 0.5);
                        world.add(sphere(center, 0.2, metal(color, fuzz)));
                    } else {
                        world.add(sphere(center, 0.2, dialectric(1.5)));
                    }
                }
            }
        }

        world.add(sphere(vec(0.0, 1.0, 0.0), 1.0, dialectric(1.5f)));
        world.add(sphere(vec(-4.0, 1.0, 0.0), 1.0, lambertian(vec(0.4, 0.2, 0.1))));
        world.add(sphere(vec(4.0, 1.0, 0.0), 1.0, metal(vec(0.7, 0.6, 0.5), 0.0)));

        TracerSettings settings = new TracerSettings();
        settings.aspectRatio = 3.0 / 2.0;
        settings.position = vec(13.0, 2.0, 3.0);
        settings.target = ORIGIN;
        settings.up = vec(0.0, 1.0, 0.0);
        settings.focalDistance = 10.0;
        settings.aperture = 0.1;
        settings.imageWidth = 1000;
        settings.useBoundingVolume = true;
        settings.samplesPerPixel = 500;
        settings.bouncesPerPixel = 50;
        return new Tracer(settings, world);
    }

    public static Tracer defaultThreeBalls() {
        return new Tracer(new TracerSettings(), threeBalls());
    }
}
