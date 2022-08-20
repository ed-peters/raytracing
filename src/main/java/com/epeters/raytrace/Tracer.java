package com.epeters.raytrace;

import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.Utils.BLACK;
import static com.epeters.raytrace.Utils.SKY_BLUE;
import static com.epeters.raytrace.Utils.WHITE;
import static com.epeters.raytrace.Utils.random;
import static com.epeters.raytrace.Utils.sqrt;
import static com.epeters.raytrace.Utils.time;
import static com.epeters.raytrace.Vector.ORIGIN;
import static com.epeters.raytrace.Vector.randomInUnitSphere;

public class Tracer {

    public static final int SAMPLES_PER_PIXEL = 100;
    public static final int BOUNCES_PER_PIXEL = 50;

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final Camera camera;
    private final List<Hittable> world;

    public Tracer(Camera camera, List<Hittable> world) {
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
        Hit hit = Hittable.computeHit(world, ray);
        if (hit == null) {
            return backgroundColor(ray);
        }

        // if we do hit, we'll simulate a bouncing light ray and recurse
        Vector hitPoint = hit.point();
        Vector hitNormal = hit.normal();
        Vector bounceDirection = hitNormal.plus(randomInUnitSphere());
        Ray bounceRay = new Ray(hitPoint, bounceDirection);
        return computeColor(bounceRay, bouncesRemaining - 1).mul(0.5);
    }

    /** Computes the background color for the specified ray */
    private Vector backgroundColor(Ray ray) {
        double t = 0.5f * (ray.direction().normalize().y() + 1.0);
        return WHITE.mul(1.0 - t).plus(SKY_BLUE.mul(t));
    }

    public static void main(String [] args) {

        List<Hittable> world = new ArrayList<>();
        world.add(new Sphere(new Vector(0.0, 0.0, -1.0), 0.5));
        world.add(new Sphere(new Vector(0.0, -100.5, -1.0), 100.0));

        Camera camera = new Camera(ORIGIN);
        Tracer tracer = new Tracer(camera, world);

        Image image = time(() -> tracer.render(800));
        image.writePpm("/Users/ed.peters/Desktop/trace.ppm");
    }
}
