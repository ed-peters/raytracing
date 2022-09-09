package com.epeters.raytrace;

import com.epeters.raytrace.hittables.HittableVolume;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.hittables.Solids;
import com.epeters.raytrace.surfaces.Texture;
import com.epeters.raytrace.utils.Axis;
import com.epeters.raytrace.utils.Mesh;
import com.epeters.raytrace.utils.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.hittables.Solids.tmesh;
import static com.epeters.raytrace.surfaces.Material.light;
import static com.epeters.raytrace.hittables.Solids.box;
import static com.epeters.raytrace.hittables.Solids.lsphere;
import static com.epeters.raytrace.hittables.Solids.rect;
import static com.epeters.raytrace.hittables.Solids.sphere;
import static com.epeters.raytrace.hittables.Solids.tsphere;
import static com.epeters.raytrace.surfaces.Material.norm;
import static com.epeters.raytrace.surfaces.Texture.checker;
import static com.epeters.raytrace.surfaces.Texture.image;
import static com.epeters.raytrace.surfaces.Texture.noise;
import static com.epeters.raytrace.surfaces.Texture.solid;
import static com.epeters.raytrace.utils.Utils.BLACK;
import static com.epeters.raytrace.utils.Utils.DARK_GREEN;
import static com.epeters.raytrace.utils.Utils.MID_GRAY;
import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.randomVector;
import static com.epeters.raytrace.utils.Utils.randomVectorInUnitCube;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;
import static com.epeters.raytrace.utils.Utils.RED;
import static com.epeters.raytrace.utils.Utils.BLUE;
import static com.epeters.raytrace.surfaces.Material.lambertian;
import static com.epeters.raytrace.surfaces.Material.metal;
import static com.epeters.raytrace.surfaces.Material.dialectric;
import static com.epeters.raytrace.surfaces.Texture.turbulence;
import static com.epeters.raytrace.utils.XYZPlane.XZ;
import static com.epeters.raytrace.utils.XYZPlane.YZ;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

import static com.epeters.raytrace.hittables.Solids.fog;
import static com.epeters.raytrace.utils.XYZPlane.XY;

public class Scenes {

    public static final Material M_MID_GRAY = lambertian(MID_GRAY);
    public static final Material M_RED = lambertian(RED);
    public static final Material M_BLUE = lambertian(BLUE);
    public static final Material M_NOISE = lambertian(turbulence(WHITE, 4.0, 7));
    public static final Material M_LIGHT = light(vec(4.0, 4.0, 4.0));

    // ====================================================================================
    // triangles!
    // ====================================================================================

    public static SceneConfig triangles() {

        Mesh mesh = new Mesh();
        mesh.put("a", vec(-4.0, -4.0, -6.0));
        mesh.put("b", vec(-4.0, 4.0, -6.0));
        mesh.put("c", vec(4.0, -4.0, -7.0));
        mesh.put("d", vec(4.0, 4.0, -7.0));
        mesh.put("e", vec(0.0, 1.0, -3.0));
        mesh.addTriangle("a", "b", "e");
        mesh.addTriangle("b", "d", "e");
        mesh.addTriangle("e", "c", "d");

        SceneConfig config = new SceneConfig();
        config.add(tmesh(M_MID_GRAY, mesh));
        return config;
    }

    public static SceneConfig gourd() {
        SceneConfig config = new SceneConfig();
        config.add(tmesh(norm(), "/gourd.obj"));
        config.position = vec(0.0, 0.0, 4.0);
        config.target = vec(0.0, 0.0, 3.0);
        return config;
    }

    // ====================================================================================
    // two balls
    // ====================================================================================

    public static SceneConfig twoGrayBalls() {

        SceneConfig config = new SceneConfig();
        config.add(lsphere(vec(0.0, 0.0, -1.0), 0.5, MID_GRAY));
        config.add(lsphere(vec(0.0, -100.5, -1.0), 100.0, MID_GRAY));
        return config;
    }

    // ====================================================================================
    // lights
    // ====================================================================================

    public static SceneConfig lightedMarble() {
        SceneConfig config = new SceneConfig();
        config.add(rect(XY, 1.0, 1.0, 3.0, 3.0, -4.0, M_LIGHT));
        config.add(tsphere(vec(0.0, -1000.0, 0.0), 1000.0, 4.0, 7));
        config.add(tsphere(vec(0.0, 2.0, 0.0), 2.0, 4.0, 7));
        config.position = vec(26.0, 3.0, 6.0);
        config.target = vec(0.0, 2.0, 0.0);
        config.fieldOfView = 20.0;
        config.backgroundColor = (r) -> ORIGIN;
        return config;
    }

    // ====================================================================================
    // boxes
    // ====================================================================================

    public static SceneConfig grayBox() {

        SceneConfig config = new SceneConfig();

        Hittable box = Solids.box(vec(-1.0, -1.0, -3.0), vec(1.0, 1.0, -4.0), M_MID_GRAY);
        box = box.rotate(Axis.Y, 45.0);

        config.add(box);
        config.position = vec(2.0, 2.0, 0.0);
        config.target = vec(0.0, 0.0, -3.5);
        return config;
    }

    public static SceneConfig cornellBox() {

        Material red = lambertian(solid(vec(0.65, 0.05, 0.05)));
        Material white = lambertian(solid(vec(0.73, 0.73, 0.73)));
        Material green = lambertian(solid(vec(0.12, 0.45, 0.15)));
        Material light = light(vec(15.0, 15.0, 15.0));

        SceneConfig config = new SceneConfig();
        config.add(rect(YZ, 0.0, 0.0, 555.0, 555.0, 555.0, green));
        config.add(rect(YZ, 0.0, 0.0, 555.0, 555.0, 0.0, red));
        config.add(rect(XZ, 0.0, 0.0, 555.0, 555.0, 0.0, white));
        config.add(rect(XZ, 0.0, 0.0, 555.0, 555.0, 555.0, white));
        config.add(rect(XY, 0.0, 0, 555.0, 555.0, 555.0, white));
        config.add(rect(XY, 200.0, 150.0, 355.0, 305.0, 554.0, light));
        config.aspectRatio = 1.0;
        config.position = vec(278.0, 278.0, -800.0);
        config.target = vec(278.0, 278.0, 0.0);
        config.fieldOfView = 40.0;
        config.backgroundColor = (r) -> BLACK;
        return config;
    }

    public static SceneConfig cornellBoxWithObjects(boolean fog) {

        Material red = lambertian(solid(vec(0.65, 0.05, 0.05)));
        Material white = lambertian(solid(vec(0.73, 0.73, 0.73)));
        Material green = lambertian(solid(vec(0.12, 0.45, 0.15)));
        Material light = light(vec(15.0, 15.0, 15.0));

        SceneConfig config = new SceneConfig();

        Hittable box1 = box(vec(0, 0.0, 0), vec(165.0, 330.0, 165.0), white);
        box1 = box1.rotate(Axis.Y, 15.0);
        box1 = box1.translate(vec(265.0, 0.0, 295.0));
        if (fog) {
            box1 = fog(box1, 0.01, BLACK);
        }
        config.add(box1);

        Hittable box2 = box(vec(0, 0.0, 0), vec(165.0, 165.0, 165.0), white);
        box2 = box2.rotate(Axis.Y, -18.0);
        box2 = box2.translate(vec(130.0, 0.0, 65.0));
        if (fog) {
            box2 = fog(box2, 0.01, WHITE);
        }
        config.add(box2);

        config.add(rect(YZ, 0.0, 0.0, 555.0, 555.0, 555.0, green));
        config.add(rect(YZ, 0.0, 0.0, 555.0, 555.0, 0.0, red));
        config.add(rect(XZ, 0.0, 0.0, 555.0, 555.0, 0.0, white));
        config.add(rect(XZ, 0.0, 0.0, 555.0, 555.0, 555.0, white));
        config.add(rect(XY, 0.0, 0, 555.0, 555.0, 555.0, white));
        config.add(rect(XZ, 213.0, 227.0, 343.0, 332.0, 554.0, light));
        config.aspectRatio = 1.0;
        config.position = vec(278.0, 278.0, -800.0);
        config.target = vec(278.0, 278.0, 0.0);
        config.fieldOfView = 40.0;
        config.backgroundColor = (r) -> BLACK;
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
        config.add(sphere(vec(-r, 0.0, -1.0), r, M_BLUE));
        config.add(sphere(vec(r, 0.0, -1.0), r, M_RED));
        config.fieldOfView = 90.0;
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    // ====================================================================================
    // closeup checkered spheres
    // ====================================================================================

    public static SceneConfig closeupCheckeredSpheres() {
        Material checks = lambertian(checker(WHITE, DARK_GREEN));
        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -10.0, 0.0), 10.0, checks));
        config.add(sphere(vec(0.0, 10.0, 0.0), 10.0, checks));
        config.position = vec(13.0, 2.0, 3.0);
        config.target = vec(0.0, 0.0, 0.0);
        config.fieldOfView = 20.0;
        return config;
    }

    // ====================================================================================
    // noisy world
    // ====================================================================================

    public static SceneConfig noisySphere() {
        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, M_NOISE));
        config.add(sphere(vec(0.0, 2.0, 0.0), 2.0, M_NOISE));
        config.position = vec(13,2,3);
        config.target = vec(0,0,0);
        config.fieldOfView = 20.0;
        return config;
    }

    public static SceneConfig marbleSphere() {
        Material marble = lambertian(turbulence(WHITE, 4.0, 7));
        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, -1000.0, 0.0), 1000.0, marble));
        config.add(sphere(vec(0.0, 2.0, 0.0), 2.0, marble));
        config.position = vec(13,2,3);
        config.target = vec(0,0,0);
        config.fieldOfView = 20.0;
        return config;
    }

    // ====================================================================================
    // image textures
    // ====================================================================================

    public static SceneConfig earth() {
        SceneConfig config = new SceneConfig();
        config.add(sphere(vec(0.0, 0.0, 0.0), 2.0, lambertian(image("/earth.jpg"))));
        config.position = vec(13,2,3);
        config.target = vec(0,0,0);
        config.fieldOfView = 20.0;
        return config;
    }

    // ====================================================================================
    // random world
    // ====================================================================================

    public static Vector randomColor() {
        return randomVectorInUnitCube().mul(randomVectorInUnitCube());
    }

    public static SceneConfig randomWorld() {

        Texture checker = Texture.checker(WHITE, MID_GRAY);

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
        config.aspectRatio = 3.0 / 2.0;
        return config;
    }

    // ====================================================================================
    // finale
    // ====================================================================================

    public static SceneConfig finale() {

        SceneConfig config = new SceneConfig();

        Material light = light(vec(7.0, 7.0, 7.0));
        config.add(rect(XZ, 100, 220, 300, 420, 550, light));

        List<Hittable> boxes = new ArrayList<>();
        Material ground = lambertian(vec(0.48, 0.83, 0.53));
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {

                double w = 100.0;

                double x0 = -1000.0 + i * w;
                double y0 = 0.0;
                double z0 = -1000.0 + j * w;

                double x1 = x0 + w;
                double y1 = random(1.0, 101.0);
                double z1 = z0 + w;

                config.add(box(vec(x0, y0, z0), vec(x1, y1, z1), ground));
            }
        }

        config.add(sphere(vec(400, 400, 200), 50, lambertian(vec(0.7, 0.3, 0.1))));
        config.add(sphere(vec(260, 150, 45), 50, dialectric(1.5)));
        config.add(sphere(vec(0, 150, 145), 50, metal(vec(0.8, 0.8, 0.9), 1.0)));
        config.add(sphere(vec(400, 200, 400), 100, lambertian(image("/earth.jpg"))));
        config.add(sphere(vec(220, 280, 300), 80, lambertian(noise(WHITE, 0.1))));

        Hittable b1 = sphere(vec(360, 150, 145), 70, dialectric(1.5));
        config.add(b1);
        config.add(fog(b1, 0.2, vec(0.2, 0.4, 0.9)));
//
//        Solid b2 = sphere(vec(0, 0, 0), 5000, dialectric(1.5));
//        config.add(fog(b2, 0.0001, WHITE));

        Material offwhite = lambertian(vec(0.73, 0.73, 0.73));
        List<Hittable> boxes2 = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            boxes2.add(sphere(randomVector(0, 165), 10, offwhite));
        }
        Hittable list = HittableVolume.from(boxes2);
        list = list.translate(vec(-100, 270, 395));
        list = list.rotate(Axis.Y, 15);
        config.add(list);

        config.aspectRatio = 16.0 / 9.0;
        config.backgroundColor = (r) -> BLACK;
        config.position = vec(478, 300, -600);
        config.target = vec(278, 278, 0);
        config.fieldOfView = 40.0;

        return config;
    }
}
