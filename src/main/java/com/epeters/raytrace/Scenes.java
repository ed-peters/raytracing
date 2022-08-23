package com.epeters.raytrace;

import com.epeters.raytrace.rendering.RendererConfig;
import com.epeters.raytrace.solids.Material;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.solids.Solid.movingSphere;
import static com.epeters.raytrace.utils.Utils.MID_GRAY;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.randomVectorInUnitCube;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;
import static com.epeters.raytrace.utils.Utils.RED;
import static com.epeters.raytrace.utils.Utils.BLUE;
import static com.epeters.raytrace.solids.Material.lambertian;
import static com.epeters.raytrace.solids.Material.metal;
import static com.epeters.raytrace.solids.Material.dialectric;
import static com.epeters.raytrace.solids.Solid.sphere;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Scenes {

    public static RendererConfig threeBalls() {

        Material ground = lambertian(vec(0.8, 0.8, 0.0));
        Material left = dialectric(1.5);
        Material center = lambertian(vec(0.1, 0.2, 0.5));
        Material right = metal(vec(0.8, 0.6, 0.2), 0.0);

        RendererConfig config = new RendererConfig();
        config.add(sphere(ground, vec(0.0, -100.5, -1.0), 100.0));
        config.add(sphere(left, vec(-1.0, 0.0, -1.0), 0.5));
        config.add(sphere(left, vec(-1.0, 0.0, -1.0), -0.4));
        config.add(sphere(center, vec(0.0, 0.0, -1.0), 0.5));
        config.add(sphere(right, vec(1.0, 0.0, -1.0), 0.5));
        return config;
    }

    public static RendererConfig closeupSpheres() {
        double r = cos(PI / 4.0);
        RendererConfig config = new RendererConfig();
        config.add(sphere(lambertian(BLUE), vec(-r, 0.0, -1.0), r));
        config.add(sphere(lambertian(RED), vec(r, 0.0, -1.0), r));
        config.fieldOfView = 90.0;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    public static RendererConfig farawayThreeBalls() {
        RendererConfig config = threeBalls();
        config.position = vec(-2.0,2.0,1.0);
        config.target = vec(0.0,0.0,-1.0);
        config.up = vec(0.0, 1.0, 0.0);
        config.fieldOfView = 90.0;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    public static RendererConfig closeupThreeBalls() {
        RendererConfig config = threeBalls();
        config.position = vec(-2.0,2.0,1.0);
        config.target = vec(0.0,0.0,-1.0);
        config.up = vec(0.0, 1.0, 0.0);
        config.fieldOfView = 20;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    public static RendererConfig fuzzyThreeBalls() {
        RendererConfig config = threeBalls();
        config.position = vec(3.0, 3.0, 2.0);
        config.target = vec(0.0, 0.0, -1.0);
        config.up = vec(0.0, 1.0, 0.0);
        config.aperture = 2.0;
        config.fieldOfView = 20.0;
        return config;
    }

    public static RendererConfig randomWorld(boolean motionBlur) {

        RendererConfig config = new RendererConfig();
        config.motionBlur = motionBlur;

        config.add(sphere(lambertian(MID_GRAY), vec(0.0, -1000.0, 0.0), 1000.0));

        Vector comp = vec(4.0, 0.2, 0.0);

        for (int a=-11; a<11; a++) {
            for (int b=-11; b<11; b++) {

                Vector center = vec(a + 0.9 * random(), 0.2, b + 0.9 * random());
                if (center.minus(comp).length() > 0.9) {
                    double material = random();
                    if (material < 0.8) {
                        Vector color = randomColor();
                        if (motionBlur) {
                            Vector centerEnd = center.plus(vec(0.0, random(0.0, 0.5), 0.0));
                            config.add(movingSphere(lambertian(color), center, centerEnd, 0.2));
                        } else {
                            config.add(sphere(lambertian(color), center, 0.2));
                        }
                    } else if (material < 0.95) {
                        Vector color = randomColor();
                        double fuzz = random(0.0, 0.5);
                        config.add(sphere(metal(color, fuzz), center, 0.2));
                    } else {
                        config.add(sphere(dialectric(1.5), center, 0.2));
                    }
                }
            }
        }

        config.add(sphere(dialectric(1.5f), vec(0.0, 1.0, 0.0), 1.0));
        config.add(sphere(lambertian(vec(0.4, 0.2, 0.1)), vec(-4.0, 1.0, 0.0), 1.0));
        config.add(sphere(metal(vec(0.7, 0.6, 0.5), 0.0), vec(4.0, 1.0, 0.0), 1.0));
        config.aspectRatio = 3.0 / 2.0;
        config.position = vec(13.0, 2.0, 3.0);
        config.target = ORIGIN;
        config.up = vec(0.0, 1.0, 0.0);
        config.focalDistance = 10.0;
        config.aperture = 0.1;
        config.samplesPerPixel = 500;
        config.bouncesPerPixel = 50;
        return config;
    }

    public static Vector randomColor() {
        return randomVectorInUnitCube().mul(randomVectorInUnitCube());
    }
}
