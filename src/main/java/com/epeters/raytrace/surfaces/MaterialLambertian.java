package com.epeters.raytrace.surfaces;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.hittables.Scatter;
import com.epeters.raytrace.utils.Basis;
import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Vector;

import static com.epeters.raytrace.utils.Utils.randomCosineDirection;
import static com.epeters.raytrace.utils.Utils.randomUnitVector;

public final class MaterialLambertian implements Material {

    enum Mode {
        SIMPLE,
        SAMPLED,
        HEMISPHERE,
        ORTHO
    }

    public static final Mode MODE = Mode.ORTHO;

    private final Texture texture;

    public MaterialLambertian(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Scatter computeScatter(Ray ray, Hit hit) {
        return switch (MODE) {
            case SIMPLE -> simpleBounce(ray, hit);
            case SAMPLED -> sampledBounce(ray, hit);
            case HEMISPHERE -> hemisphereBounce(ray, hit);
            case ORTHO -> orthonormalBounce(ray, hit);
        };
    }

    @Override
    public double computeScatterPdf(Ray ray, Hit hit, Ray scatter) {
        switch (MODE) {
            case SIMPLE -> {
                return 0.0;
            }
            default -> {
                double cos = hit.normal().dot(scatter.direction());
                return cos < 0 ? 0 : cos / Math.PI;
            }
        }
    }

    private Scatter simpleBounce(Ray ray, Hit hit) {
        Color albedo = texture.calculateColor(hit);
        Vector bounce = hit.normal().plus(randomUnitVector());
        if (bounce.zeroish()) {
            bounce = hit.normal();
        }
        return Scatter.simpleBounce(albedo, bounce);
    }

    private Scatter sampledBounce(Ray ray, Hit hit) {
        Color albedo = texture.calculateColor(hit);
        Vector bounce = hit.normal().plus(randomUnitVector()).normalize();
        if (bounce.zeroish()) {
            bounce = hit.normal();
        }
        double pdf = hit.normal().dot(bounce) / Math.PI;
        return Scatter.diffuse(albedo, bounce, pdf);
    }

    private Scatter hemisphereBounce(Ray ray, Hit hit) {
        Color albedo = texture.calculateColor(hit);
        Vector bounce = hit.normal().randomHemisphericReflection();
        double pdf = 0.5 / Math.PI;
        return Scatter.diffuse(albedo, bounce, pdf);
    }

    private Scatter orthonormalBounce(Ray ray, Hit hit) {
        Color albedo = texture.calculateColor(hit);
        Basis basis = Basis.fromW(hit.normal());
        Vector bounce = basis.local(randomCosineDirection()).normalize();
        double pdf = basis.w().dot(bounce) / Math.PI;
        return Scatter.diffuse(albedo, bounce, pdf);
    }
}
