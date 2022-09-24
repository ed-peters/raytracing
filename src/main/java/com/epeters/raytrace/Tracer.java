package com.epeters.raytrace;

import com.epeters.raytrace.hittables.HittableVolume;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Scatter;
import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Pdf;

import java.util.function.Function;

import static com.epeters.raytrace.utils.Color.BLACK;
import static com.epeters.raytrace.utils.Utils.coalesce;
import static com.epeters.raytrace.utils.Utils.random;

public final class Tracer {

    private final int samplesPerPixel;
    private final int bouncesPerPixel;
    private final double sampleScale;
    private final Camera camera;
    private final Hittable world;
    private final int imageWidth;
    private final int imageHeight;
    private final Function<Ray,Color> backgroundColor;
    private final Hittable light;

    public Tracer(SceneConfig config) {
        this.samplesPerPixel = config.samplesPerPixel;
        this.bouncesPerPixel = config.bouncesPerPixel;
        this.sampleScale = 1.0 / samplesPerPixel;
        this.camera = new Camera(config);
        this.world = HittableVolume.from(config);
        this.imageWidth = config.imageWidth;
        this.imageHeight = (int)(imageWidth / config.aspectRatio);
        this.backgroundColor = config.backgroundColor;
        this.light = config.light;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    /** Renders a single pixel of the output image */
    public Color renderPixel(int x, int y) {

        Color color = Color.BLACK;

        for (int s=0; s<samplesPerPixel; s++) {

            double u = (x + random(-0.5, 0.5)) / (double) (imageWidth - 1);
            double v = (y + random(-0.5, 0.5)) / (double) (imageHeight - 1);
            Ray ray = camera.computeRay(u, v);

            color = color.plus(computeColor(ray, bouncesPerPixel));
        }

        return color.normalize(samplesPerPixel);
    }

    /** Determines the color of the scene at the point hit by the specified ray */
    private Color computeColor(Ray ray, int bouncesRemaining) {

        // if we're out of bounces, this ray won't contribute any color
        if (bouncesRemaining < 1) {
            return BLACK;
        }

        // if we don't hit, this ray will contribute the background color
        Hit hit = world.intersect(ray, 1e-8, Double.MAX_VALUE);
        if (hit == null) {
            return backgroundColor.apply(ray);
        }

        Material material = hit.material();
        Scatter scatter = material.computeScatter(ray, hit);

        switch (scatter.type()) {

            case EMISSIVE:
                return scatter.emission();

            case DIFFUSE: {
                Pdf pdf0 = Pdf.hittable(light, hit.point());
                Pdf pdf1 = Pdf.cosine(hit.normal());
                Pdf pdf = Pdf.mix(pdf0, pdf1);

                Ray bounceRay = new Ray(hit.point(), pdf.generate());
                Color bounceColor = computeColor(bounceRay, bouncesRemaining - 1);
                double pmf = material.computeScatterPdf(ray, hit, bounceRay);
                return scatter.emission().plus(scatter.attenuation().mul(pmf).mul(bounceColor).div(pdf.value(bounceRay.direction())));
            }

            case SPECULAR:
                throw new UnsupportedOperationException();

        }

        throw new UnsupportedOperationException();
    }
}
