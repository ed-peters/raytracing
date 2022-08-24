package com.epeters.raytrace;

import com.epeters.raytrace.hittables.BoundingBox;
import com.epeters.raytrace.hittables.BoundingVolume;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.HitInfo;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.hittables.HittableList;
import com.epeters.raytrace.renderer.Renderer;
import com.epeters.raytrace.solids.Solid;

import java.util.List;

import static com.epeters.raytrace.Utils.BLACK;
import static com.epeters.raytrace.Utils.SKY_BLUE;
import static com.epeters.raytrace.Utils.WHITE;
import static com.epeters.raytrace.Utils.random;
import static com.epeters.raytrace.Utils.sqrt;
import static com.epeters.raytrace.Vector.vec;

public class Tracer {

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final Camera camera;
    private final Hittable world;
    private final double aspectRatio;
    private final int imageWidth;
    private final int imageHeight;

    public Tracer(TracerSettings settings, List<Solid> world) {
        this.samplesPerPixel = settings.samplesPerPixel;
        this.bouncesPerPixel = settings.bouncesPerPixel;
        this.sampleScale = 1.0 / samplesPerPixel;
        this.aspectRatio = settings.aspectRatio;
        this.camera = new Camera(settings);
        this.world = settings.useBoundingVolume ? BoundingVolume.from(world) : new HittableList(world);
        this.imageWidth = settings.imageWidth;
        this.imageHeight = (int)(imageWidth / settings.aspectRatio);
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    /** Renders a single pixel of the output image */
    public Vector renderPixel(int x, int y) {

        double pixelX = 0.0;
        double pixelY = 0.0;
        double pixelZ = 0.0;

        for (int s=0; s<samplesPerPixel; s++) {

            double u = (x + random(-0.5, 0.5)) / (double) (imageWidth - 1);
            double v = (y + random(-0.5, 0.5)) / (double) (imageHeight - 1);
            Ray ray = camera.computeRay(u, v);

            Vector color = computeColor(ray, bouncesPerPixel);
            pixelX += color.x();
            pixelY += color.y();
            pixelZ += color.z();
        }

        pixelX = sqrt(pixelX * sampleScale);
        pixelY = sqrt(pixelY * sampleScale);
        pixelZ = sqrt(pixelZ * sampleScale);
        return vec(pixelX, pixelY, pixelZ);
    }

    /** Determines the color of the scene at the point hit by the specified ray */
    private Vector computeColor(Ray ray, int bouncesRemaining) {

        // if we're out of bounces, this ray won't contribute any color
        if (bouncesRemaining < 1) {
            return BLACK;
        }

        // if we don't hit, this ray will contribute the background color
        Hit hit = world.hit(ray, 1e-8, Double.MAX_VALUE);
        if (hit == null) {
            return backgroundColor(ray);
        }

        // if we do have a hit, let's find out more
        HitInfo info = hit.info().get();

        // if the solid doesn't have a material, we'll do normal shading
        if (info.getColor() == null) {
            return defaultColor(info);
        }

        // if there's no scattering, we're a solid color
        if (info.getBounce() == null) {
            return info.getColor();
        }

        Ray bounceRay = new Ray(info.getPoint(), info.getBounce());
        return info.getColor().mul(computeColor(bounceRay, bouncesRemaining-1));
    }

    /** Computes the background color for the specified ray */
    private Vector backgroundColor(Ray ray) {
        double t = 0.5f * (ray.direction().normalize().y() + 1.0);
        return WHITE.mul(1.0 - t).plus(SKY_BLUE.mul(t));
    }

    /** Computes the default color for an object with no material */
    private Vector defaultColor(HitInfo info) {
        return info.getNormal().plus(WHITE).mul(0.5);
    }

    public static void main(String [] args) {

        int threads = Runtime.getRuntime().availableProcessors() - 1;

        Tracer tracer = Scenes.randomWorld();
        Renderer renderer = new Renderer(tracer, "trace.png", threads);
        Utils.time(() -> renderer.render());
    }
}
