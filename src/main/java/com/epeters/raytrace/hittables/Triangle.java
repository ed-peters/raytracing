package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.surfaces.MaterialParams;
import com.epeters.raytrace.utils.Box;
import com.epeters.raytrace.utils.Vector;

public class Triangle implements Hittable {

    private final Material material;
    private final Box bounds;
    private final Vector vert0;
    private final Vector edge1;
    private final Vector edge2;
    private final Vector norm;
    private final double d;

    public Triangle(Material material, Vector vert0, Vector vert1, Vector vert2) {
        this.material = material;
        this.bounds = Box.around(vert0, vert1, vert2);
        this.vert0 = vert0;
        this.edge1 = vert1.minus(vert0);
        this.edge2 = vert2.minus(vert0);
        this.norm = edge1.cross(edge2).normalize();
        this.d = norm.dot(vert0);
    }

    @Override
    public Box getBounds() {
        return this.bounds;
    }

    @Override
    public Hit intersect(Ray ray, double tmin, double tmax) {

        if (bounds.doesNotIntersect(ray, tmin, tmax)) {
            return null;
        }

        double tn = d - ray.origin().dot(norm);
        double td = norm.dot(ray.direction());
        double t = tn / td;
        if (t < tmin || t > tmax) {
            return null;
        }

        Vector pvec = ray.direction().cross(edge2);

        double det = edge1.dot(pvec);
        double invdet = 1.0 / det;

        Vector tvec = ray.origin().minus(vert0);
        double u = tvec.dot(pvec) * invdet;
        if (u < 0.0 || u > 1.0) {
            return null;
        }

        Vector qvec = tvec.cross(edge1);
        double v = ray.direction().dot(qvec) * invdet;
        if (v < 0.0 || u + v > 1.0) {
            return null;
        }

        return new Hit(ray, t, (p, i) -> computeColor(p, i, u, v));
    }

    private HitColor computeColor(Vector point, Vector incoming, double u, double v) {
        MaterialParams params = MaterialParams.from(point, incoming, norm, u, v);
        return material.computeHitColor(params);
    }
}
