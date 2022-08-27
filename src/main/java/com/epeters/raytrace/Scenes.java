package com.epeters.raytrace;

import com.epeters.raytrace.materials.LightedMaterial;
import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.solids.XYRectangle;
import com.epeters.raytrace.textures.CheckeredTexture;
import com.epeters.raytrace.textures.ImageTexture;
import com.epeters.raytrace.textures.Texture;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.materials.Material.light;
import static com.epeters.raytrace.solids.Solid.rect;
import static com.epeters.raytrace.utils.Utils.DARK_GREEN;
import static com.epeters.raytrace.utils.Utils.DARK_PINK;
import static com.epeters.raytrace.utils.Utils.MID_GRAY;
import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.randomVectorInUnitCube;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;
import static com.epeters.raytrace.utils.Utils.RED;
import static com.epeters.raytrace.utils.Utils.BLUE;
import static com.epeters.raytrace.materials.Material.lambertian;
import static com.epeters.raytrace.materials.Material.metal;
import static com.epeters.raytrace.materials.Material.dialectric;
import static com.epeters.raytrace.solids.Solid.sphere;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Scenes {

    // ====================================================================================
    // two balls
    // ====================================================================================

    public static SceneConfig twoGrayBalls() {

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, 0.0, -1.0), 0.5, lambertian(MID_GRAY)));
        config.add(sphere(vec(0.0, -100.5, -1.0), 100.0, lambertian(MID_GRAY)));
        return config;
    }

    public static SceneConfig redBox() {

        Texture noise = Texture.turbulence(WHITE, 4.0, 7);
        Texture earth = new ImageTexture("/earth.jpg");

        SceneConfig config = new SceneConfig();
        config.add(rect(-1.0, -1.0, 1.0, 1.0, -3.0, light(vec(6.0, 6.0, 6.0))));
        config.add(sphere(vec(0.0, 2.0, 1.0), 2.0, lambertian(earth)));
        config.add(sphere(vec(0.0, -100.5, 1.0), 100.0, lambertian(MID_GRAY)));
        config.position = vec(11.0, 2.0, 2.0);
        config.target = ORIGIN;
        config.fieldOfView = 20.0;
        config.backgroundColor = (r) -> ORIGIN;
        return config;
    }

    // ====================================================================================
    // three balls
    // ====================================================================================

    public static SceneConfig defaultThreeBalls() {

        Material ground = lambertian(vec(0.8, 0.8, 0.0));
        Material left = dialectric(1.5);
        Material center = lambertian(vec(0.1, 0.2, 0.5));
        Material right = metal(vec(0.8, 0.6, 0.2), 0.0);

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -100.5, -1.0), 100.0, ground));
        config.add(sphere(vec(-1.0, 0.0, -1.0), 0.5, left));
        config.add(sphere(vec(-1.0, 0.0, -1.0), -0.4, left));
        config.add(sphere(vec(0.0, 0.0, -1.0), 0.5, center));
        config.add(sphere(vec(1.0, 0.0, -1.0), 0.5, right));
        return config;
    }

    public static SceneConfig farawayThreeBalls() {
        SceneConfig config = defaultThreeBalls();
        config.position = vec(-2.0,2.0,1.0);
        config.target = vec(0.0,0.0,-1.0);
        config.up = vec(0.0, 1.0, 0.0);
        config.fieldOfView = 90.0;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    public static SceneConfig closeupThreeBalls() {
        SceneConfig config = defaultThreeBalls();
        config.position = vec(-2.0,2.0,1.0);
        config.target = vec(0.0,0.0,-1.0);
        config.up = vec(0.0, 1.0, 0.0);
        config.fieldOfView = 20;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    public static SceneConfig fuzzyThreeBalls() {
        SceneConfig config = defaultThreeBalls();
        config.position = vec(3.0, 3.0, 2.0);
        config.target = vec(0.0, 0.0, -1.0);
        config.up = vec(0.0, 1.0, 0.0);
        config.aperture = 2.0;
        config.fieldOfView = 20.0;
        return config;
    }

    // ====================================================================================
    // closeup red and blue spheres
    // ====================================================================================

    public static SceneConfig closeupSpheres() {

        double r = cos(PI / 4.0);

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(-r, 0.0, -1.0), r, lambertian(BLUE)));
        config.add(sphere(vec(r, 0.0, -1.0), r, lambertian(RED)));
        config.fieldOfView = 90.0;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    // ====================================================================================
    // closeup checkered spheres
    // ====================================================================================

    public static SceneConfig closeupCheckeredSpheres() {

        Texture checker = Texture.checker(WHITE, DARK_GREEN);

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -10.0, 0.0), 10.0, lambertian(checker)));
        config.add(sphere(vec(0.0, 10.0, 0.0), 10.0, lambertian(checker)));
        config.position = vec(13.0, 2.0, 3.0);
        config.target = vec(0.0, 0.0, 0.0);
        config.fieldOfView = 20.0;
        return config;
    }

    // ====================================================================================
    // noisy world
    // ====================================================================================

    public static SceneConfig noisySphere() {

        Texture noise = Texture.noise(WHITE, 4.0);

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, lambertian(noise)));
        config.add(sphere(vec(0.0, 2.0, 0.0), 2.0, lambertian(noise)));
        config.position = vec(13,2,3);
        config.target = vec(0,0,0);
        config.fieldOfView = 20.0;
        return config;
    }

    public static SceneConfig marbleSphere() {

        Texture noise = Texture.turbulence(WHITE, 4.0, 7);

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, lambertian(noise)));
        config.add(sphere(vec(0.0, 2.0, 0.0), 2.0, lambertian(noise)));
        config.position = vec(13,2,3);
        config.target = vec(0,0,0);
        config.fieldOfView = 20.0;
        return config;
    }

    public static SceneConfig litMarble() {

        Texture noise = Texture.turbulence(WHITE, 4.0, 7);

        SceneConfig config = new SceneConfig();
        config.add(rect(3, 5, 1, 3, -2, lambertian(RED))); // light(vec(4.0, 4.0, 4.0))));
        config.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, lambertian(noise)));
//        config.add(sphere(vec(0.0, 2.0, 0.0), 2.0, lambertian(noise)));
        config.position = vec(26,3,6);
        config.target = vec(3,5,1);
        config.fieldOfView = 20.0;
//        config.backgroundColor = (r) -> ORIGIN;
        return config;
    }

    // ====================================================================================
    // image textures
    // ====================================================================================

    public static SceneConfig earth() {

        Texture earth = new ImageTexture("/earth.jpg");

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, 0.0, 0.0), 2.0, lambertian(earth)));
        config.position = vec(13,2,3);
        config.target = vec(0,0,0);
        config.fieldOfView = 20.0;
        return config;
    }

    public static SceneConfig lightedScene() {

        Texture noise = Texture.turbulence(WHITE, 4.0, 7);

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, lambertian(noise)));
        config.add(sphere(vec(0.0, 2.0, 0.0), 2.0, lambertian(noise)));
        config.add(new XYRectangle(light(vec(4.0, 4.0, 4.0)), 3.0, 5.0, 1.0, 3.0, -2.0));
        config.position = vec(26.0, 3.0, 6.0);
        config.target = vec(0.0,2.0,0.0);
        config.fieldOfView = 20.0;
        config.backgroundColor = (h) -> ORIGIN;
        return config;
    }

    // ====================================================================================
    // random world
    // ====================================================================================

    public static Vector randomColor() {
        return randomVectorInUnitCube().mul(randomVectorInUnitCube());
    }

    public static SceneConfig randomWorld() {

        Texture checker = new CheckeredTexture(WHITE, MID_GRAY);

        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, lambertian(checker)));

        Vector comp = vec(4.0, 0.2, 0.0);

        for (int a=-11; a<11; a++) {
            for (int b=-11; b<11; b++) {

                Vector center = vec(a + 0.9 * random(), 0.2, b + 0.9 * random());
                if (center.minus(comp).length() > 0.9) {
                    double material = random();
                    if (material < 0.8) {
                        Vector color = randomColor();
                        config.add(sphere(center, 0.2, lambertian(color)));
                    } else if (material < 0.95) {
                        Vector color = randomColor();
                        double fuzz = random(0.0, 0.5);
                        config.add(sphere(center, 0.2, metal(color, fuzz)));
                    } else {
                        config.add(sphere(center, 0.2, dialectric(1.5)));
                    }
                }
            }
        }

        config.add(sphere(vec(0.0, 1.0, 0.0), 1.0, dialectric(1.5f)));
        config.add(sphere(vec(-4.0, 1.0, 0.0), 1.0, lambertian(vec(0.4, 0.2, 0.1))));
        config.add(sphere(vec(4.0, 1.0, 0.0), 1.0, metal(vec(0.7, 0.6, 0.5), 0.0)));
        config.position = vec(13.0, 2.0, 3.0);
        config.target = ORIGIN;
        config.up = vec(0.0, 1.0, 0.0);
        config.useBoundingVolume = true;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

}
