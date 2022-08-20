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

public class Tracer {

    public static final int SAMPLES_PER_PIXEL = 10;
    public static final int BOUNCES_PER_PIXEL = 10;

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final float sampleScale;
    private final Camera camera;
    private final List<Hittable> world;

    public Tracer(Camera camera, List<Hittable> world) {
        this.samplesPerPixel = SAMPLES_PER_PIXEL;
        this.bouncesPerPixel = BOUNCES_PER_PIXEL;
        this.sampleScale = 1.0f / samplesPerPixel;
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
                float newX = sqrt(value.x() * sampleScale);
                float newY = sqrt(value.y() * sampleScale);
                float newZ = sqrt(value.z() * sampleScale);
                return new Vector(newX, newY, newZ);
            });
        });
        return canvas;
    }

    /** Computes a ray from the camera through the image at the specified x,y, with a little fuzz */
    private Ray computeRay(Image canvas, int x, int y) {
        float u = (x + random(-0.5f, 0.5f)) / (float) (canvas.width - 1);
        float v = (y + random(-0.5f, 0.5f)) / (float) (canvas.height - 1);
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
        Vector bounceDirection = hitNormal.plus(hitNormal.randomHemisphericBounce());
        Ray bounceRay = new Ray(hitPoint, bounceDirection);
        return computeColor(bounceRay, bouncesRemaining - 1).mul(0.5f);
    }

    /** Computes the background color for the specified ray */
    private Vector backgroundColor(Ray ray) {
        float t = 0.5f * (ray.direction().normalize().y() + 1.0f);
        return WHITE.mul(1.0f - t).plus(SKY_BLUE.mul(t));
    }

    public static void main(String [] args) {

        List<Hittable> world = new ArrayList<>();
        world.add(new Sphere(new Vector(0.0f, 0.0f, -1.0f), 0.5f));
        world.add(new Sphere(new Vector(0.0f, -100.5f, -1.0f), 100.0f));

        Camera camera = new Camera(ORIGIN);
        Tracer tracer = new Tracer(camera, world);

        Image image = time(() -> tracer.render(800));
        image.writePpm("/Users/ed.peters/Desktop/trace.ppm");
    }
}
