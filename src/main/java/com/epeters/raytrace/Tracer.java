package com.epeters.raytrace;

import com.epeters.raytrace.geometry.Hit;
import com.epeters.raytrace.geometry.Ray;
import com.epeters.raytrace.material.Scatter;
import com.epeters.raytrace.material.Material;
import com.epeters.raytrace.utils.Vector;

import java.util.List;

import static com.epeters.raytrace.utils.Utils.BLACK;
import static com.epeters.raytrace.utils.Utils.SKY_BLUE;
import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Utils.random;
import static com.epeters.raytrace.utils.Utils.sqrt;
import static com.epeters.raytrace.utils.Utils.time;
import static com.epeters.raytrace.utils.Vector.vec;

public class Tracer {

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final Camera camera;
    private final List<Solid> world;
    private final double aspectRatio;

    public Tracer(TracerSettings settings, List<Solid> world) {
        this.samplesPerPixel = settings.samplesPerPixel;
        this.bouncesPerPixel = settings.bouncesPerPixel;
        this.sampleScale = 1.0 / samplesPerPixel;
        this.aspectRatio = settings.aspectRatio;
        this.camera = new Camera(settings);
        this.world = world;
    }

    /**
     * Renders an image of the specified width. This is the outer loop of tracing;
     * it will compute each pixel with the appropriate number of samples and then
     * scale/correct the resulting color value.
     */
    public Image render(int imageWidth) {
        Image canvas = new Image(imageWidth, (int)(imageWidth / aspectRatio));
        canvas.forEach((x,y) -> {
            if (x == 0) {
                System.err.println("working on row "+y);
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
                return vec(newX, newY, newZ);
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

        Scatter scatter = material.computeScatter(hit);
        Ray bounceRay = new Ray(hit.point(), scatter.direction());
        return scatter.attenuation().mul(computeColor(bounceRay, bouncesRemaining-1));
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
        Tracer tracer = Scenes.randomWorld();
        Image image = time(() -> tracer.render(800));
        image.writePpm("/Users/ed.peters/Desktop/trace.ppm");
    }
}
