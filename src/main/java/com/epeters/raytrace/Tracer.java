package com.epeters.raytrace;

import com.epeters.raytrace.geometry.Sphere;
import com.epeters.raytrace.material.Bounce;
import com.epeters.raytrace.material.Material;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.Utils.BLACK;
import static com.epeters.raytrace.Utils.DARK_GREEN;
import static com.epeters.raytrace.Utils.DARK_PINK;
import static com.epeters.raytrace.Utils.SKY_BLUE;
import static com.epeters.raytrace.Utils.WHITE;
import static com.epeters.raytrace.Utils.random;
import static com.epeters.raytrace.Utils.sqrt;
import static com.epeters.raytrace.Utils.time;
import static com.epeters.raytrace.Vector.ORIGIN;

public class Tracer {

    public static final int SAMPLES_PER_PIXEL = 100;
    public static final int BOUNCES_PER_PIXEL = 50;

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final Camera camera;
    private final List<Solid> world;

    public Tracer(Camera camera, List<Solid> world) {
        this.samplesPerPixel = SAMPLES_PER_PIXEL;
        this.bouncesPerPixel = BOUNCES_PER_PIXEL;
        this.sampleScale = 1.0 / samplesPerPixel;
        this.camera = camera;
        this.world = world;
    }

    /**
     * Renders an image of the specified width. This is the outer loop of tracing;
     * it will compute each pixel with the appropriate number of samples and then
     * scale/correct the resulting color value.
     */
    public Image render(int imageWidth) {
        Image canvas = new Image(imageWidth, (int)(imageWidth / camera.aspectRatio));
        canvas.forEach((x,y) -> {
            if (x == 0) {
                System.err.println("new row: "+y);
            }
            for (int s=0; s<samplesPerPixel; s++) {
                Ray ray = computeRay(canvas, x, y);
                Vector color = computeColor(ray, bouncesPerPixel);
                canvas.update(x, y, pixel -> pixel.plus(color));
            }
            canvas.update(x, y, (value) -> {
                double newX = sqrt(value.x() * sampleScale);
                double newY = sqrt(value.y() * sampleScale);
                double newZ = sqrt(value.z() * sampleScale);
                return new Vector(newX, newY, newZ);
            });
        });
        return canvas;
    }

    /** Computes a ray from the camera through the image at the specified x,y, with a little fuzz */
    private Ray computeRay(Image canvas, int x, int y) {
        double u = (x + random(-0.5, 0.5)) / (double) (canvas.width - 1);
        double v = (y + random(-0.5, 0.5)) / (double) (canvas.height - 1);
        return camera.computeRay(u, v);
    }

    /** Determines the color of the scene at the point hit by the specified ray */
    private Vector computeColor(Ray ray, int bouncesRemaining) {

        // if we're out of bounces, this ray won't contribute any color
        if (bouncesRemaining < 1) {
            return BLACK;
        }

        // if we don't hit, this ray will contribute the background color
        Hit hit = Solid.computeHit(world, ray);
        if (hit == null) {
            return backgroundColor(ray);
        }

        // if the solid doesn't have a material, we'll do normal shading
        Material material = hit.object().material();
        if (material == null) {
            return defaultColor(hit);
        }

        Bounce bounce = material.computeBounce(hit);
        Ray bounceRay = new Ray(hit.point(), bounce.direction());
        return bounce.albedo().mul(computeColor(bounceRay, bouncesRemaining-1));
    }

    /** Computes the background color for the specified ray */
    private Vector backgroundColor(Ray ray) {
        double t = 0.5f * (ray.direction().normalize().y() + 1.0);
        return WHITE.mul(1.0 - t).plus(SKY_BLUE.mul(t));
    }

    /** Computes the default color for an object with no material */
    private Vector defaultColor(Hit hit) {
        return hit.normal().plus(WHITE).mul(0.5);
    }

    public static void main(String [] args) {

        Material ground = Material.lambertian(0.8, 0.8, 0.0);
        Material center = Material.lambertian(0.7, 0.3, 0.3);
        Material left = Material.metal(0.8, 0.8, 0.8, 0.0);
        Material right = Material.metal(0.8, 0.6, 0.2, 1.0);

        List<Solid> world = new ArrayList<>();
        world.add(Solid.sphere(0.0, -100.5, -1.0, 100.0, ground));
        world.add(Solid.sphere(-1.0, 0.0, -1.0, 0.5, center));
        world.add(Solid.sphere(0.0, 0.0, -1.0, 0.5, left));
        world.add(Solid.sphere(1.0, 0.0, -1.0, 0.5, right));

        Camera camera = new Camera(ORIGIN);
        Tracer tracer = new Tracer(camera, world);

        Image image = time(() -> tracer.render(1200));
        image.writePpm("/Users/ed.peters/Desktop/trace.ppm");
    }
}
