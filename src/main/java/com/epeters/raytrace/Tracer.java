package com.epeters.raytrace;

import com.epeters.raytrace.hittables.BoundingVolume;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.HitDetails;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.hittables.HittableList;
import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.utils.Vector;

import java.util.function.Function;

import static com.epeters.raytrace.utils.Utils.BLACK;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;

public class Tracer {

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final Camera camera;
    private final Hittable world;
    private final int imageWidth;
    private final int imageHeight;
    private final Function<Ray,Vector> backgroundColor;
    private final Function<HitDetails,Vector> defaultColor;

    public Tracer(SceneConfig config) {
        this.samplesPerPixel = config.samplesPerPixel;
        this.bouncesPerPixel = config.bouncesPerPixel;
        this.sampleScale = 1.0 / samplesPerPixel;
        this.camera = new Camera(config);
        this.world = config.useBoundingVolume ? BoundingVolume.from(config) : new HittableList(config);
        this.imageWidth = config.imageWidth;
        this.imageHeight = (int)(imageWidth / config.aspectRatio);
        this.backgroundColor = config.backgroundColor;
        this.defaultColor = config.defaultColor;
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
            return backgroundColor.apply(ray);
        }

        // if we do have a hit, let's find out more
        HitDetails details = hit.solid().computeHitDetails(hit);

        // if there is no material, we'll just compute a default color
        Material material = details.solid().getMaterial();
        if (material == null) {
            return defaultColor.apply(details);
        }

        // if there is a material, we'll use it to compute onward lighting info
        Vector emission = material.computeEmission(details);
        Vector attenuation = material.computeAttenuation(details);
        Vector bounce = material.computeBounce(details);

        Vector result = ORIGIN;
        if (emission != null) {
            result = result.plus(emission);
        }
        if (attenuation != null && bounce != null) {
            Ray bounceRay = new Ray(details.point(), bounce);
            Vector bounceColor = computeColor(bounceRay, bouncesRemaining - 1);
            result = result.plus(attenuation.mul(bounceColor));
        }
        return result;
    }
}
