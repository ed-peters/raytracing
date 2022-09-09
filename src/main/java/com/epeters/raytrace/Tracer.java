package com.epeters.raytrace;

import com.epeters.raytrace.hittables.HittableVolume;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.HitColor;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.utils.Vector;

import java.util.function.Function;

import static com.epeters.raytrace.utils.Utils.BLACK;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;

public final class Tracer {

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final Camera camera;
    private final Hittable world;
    private final int imageWidth;
    private final int imageHeight;
    private final Function<Ray,Vector> backgroundColor;

    public Tracer(SceneConfig config) {
        this.samplesPerPixel = config.samplesPerPixel;
        this.bouncesPerPixel = config.bouncesPerPixel;
        this.sampleScale = 1.0 / samplesPerPixel;
        this.camera = new Camera(config);
        this.world = HittableVolume.from(config);
        this.imageWidth = config.imageWidth;
        this.imageHeight = (int)(imageWidth / config.aspectRatio);
        this.backgroundColor = config.backgroundColor;
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
        Hit hit = world.intersect(ray, 1e-8, Double.MAX_VALUE);
        if (hit == null) {
            return backgroundColor.apply(ray);
        }

        // if we do have a hit, let's find out more
        Vector point = hit.ray().at(hit.t());
        Vector incoming = hit.ray().direction();
        HitColor color = hit.color().apply(point, incoming);
        if (color == null) {
            throw new IllegalStateException("no color available from object");
        }
        return combineColor(point, color, bouncesRemaining);
    }

    private Vector combineColor(Vector point, HitColor color, int bouncesRemaining) {

        Vector e = color.emission();
        Vector a = color.attenuation();
        Vector b = color.bounce();

        Vector c = ORIGIN;
        if (e != null) {
            c = c.plus(e);
        }
        if (a != null) {
            if (b == null) {
                c = c.plus(a);
            } else {
                Ray r = new Ray(point, color.bounce());
                c = c.plus(a.mul(computeColor(r, bouncesRemaining - 1)));
            }
        }
        return c;
    }
}
