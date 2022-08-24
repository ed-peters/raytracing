package com.epeters.raytrace.rendering;

import com.epeters.raytrace.hitting.BoundingVolume;
import com.epeters.raytrace.hitting.Hit;
import com.epeters.raytrace.hitting.Hittable;
import com.epeters.raytrace.hitting.HittableList;
import com.epeters.raytrace.solids.Ray;
import com.epeters.raytrace.solids.Scatter;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.BLACK;
import static com.epeters.raytrace.utils.Utils.SKY_BLUE;
import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Implementation of the ray tracing algorithm
 */
public class Tracer {

    private final RendererConfig config;
    private final Hittable world;
    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final double widthFactor;
    private final double heightFactor;
    private final Camera camera;

    public Tracer(RendererConfig config) {
        this.config = config.init();
        this.world = config.boundingVolume ? BoundingVolume.from(config) : new HittableList(config);
        this.samplesPerPixel = config.samplesPerPixel;
        this.bouncesPerPixel = config.bouncesPerPixel;
        this.sampleScale = 1.0 / samplesPerPixel;
        this.widthFactor = config.imageWidth - 1.0;
        this.heightFactor = config.imageHeight - 1.0;
        this.camera = new Camera(config);
    }

    public RendererConfig getConfig() {
        return config;
    }

    /**
     * Renders a single pixel.
     */
    public Vector renderPixel(int x, int y) {

        Vector pixel = ORIGIN;

        for (int s=0; s<samplesPerPixel; s++) {

            // create a ray from this pixel, with a little random fuzz for each sample
            double u = (x + random(-0.5, 0.5)) / widthFactor;
            double v = (y + random(-0.5, 0.5)) / heightFactor;
            Ray ray = camera.computeRay(u, v);

            // compute color of the pixel for this ray and add it to our sample
            Vector color = colorRay(ray, bouncesPerPixel);
            pixel = pixel.plus(color);
        }

        // scale and gauss correct the pixel value
        double newX = sqrt(pixel.x() * sampleScale);
        double newY = sqrt(pixel.y() * sampleScale);
        double newZ = sqrt(pixel.z() * sampleScale);
        return vec(newX, newY, newZ);
    }

    /**
     * Determines the color of the scene at the point hit by the specified ray
     */
    private Vector colorRay(Ray ray, int bouncesRemaining) {

        // if we're out of bounces, this ray won't contribute any color
        if (bouncesRemaining < 1) {
            return BLACK;
        }

        // if we don't hit, this ray will contribute the background color
        Hit hit = world.computeHit(ray, 0.00001, Double.MAX_VALUE);
        if (hit == null) {
            return backgroundColor(ray);
        }

        // if the solid doesn't have a material, we'll do normal shading
        Scatter scatter = hit.object().scatter(hit);
        if (scatter == null) {
            return defaultColor(hit);
        }

        // otherwise, we compute the bounce ray and attenuate its color
        Ray bounceRay = new Ray(hit.point(), scatter.direction(), hit.ray().time());
        Vector bounceColor = colorRay(bounceRay, bouncesRemaining-1);
        return scatter.attenuation().mul(bounceColor);
    }

    /** Computes the background color for the specified ray */
    private Vector backgroundColor(Ray ray) {
        double t = 0.5f * (ray.direction().y() + 1.0);
        double cx = (WHITE.x() - t) + SKY_BLUE.x() * t;
        double cy = (WHITE.y() - t) + SKY_BLUE.y() * t;
        double cz = (WHITE.z() - t) + SKY_BLUE.z() * t;
        return vec(cx, cy, cz);
    }

    /** Computes the default color for an object with no material */
    private Vector defaultColor(Hit hit) {
        double cx = 0.5 * (hit.normal().x() + 1.0);
        double cy = 0.5 * (hit.normal().y() + 1.0);
        double cz = 0.5 * (hit.normal().z() + 1.0);
        return vec(cx, cy, cz);
    }
}
