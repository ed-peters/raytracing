package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.utils.Axis;
import com.epeters.raytrace.utils.Box;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of {@link Hittable} that represents an abstract volume in space. Implements a
 * recursive tree structure that speeds up intersection testing.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#boundingvolumehierarchies/thebvhnodeclass">guide</a>
 */
public  final class HittableVolume implements Hittable {

    private final Box bounds;
    private final Hittable left;
    private final Hittable right;

    public HittableVolume(Hittable left) {
        this.left = left;
        this.right = null;
        this.bounds = left.getBounds();
    }

    public HittableVolume(Hittable left, Hittable right) {
        this.left = left;
        this.right = right;
        this.bounds = Box.merge(left.getBounds(), right.getBounds());
    }

    @Override
    public Box getBounds() {
        return bounds;
    }

    @Override
    public Hit intersect(Ray ray, double tmin, double tmax) {
        if (bounds.doesNotIntersect(ray, tmin, tmax)) {
            return null;
        }
        Hit leftHit = left.intersect(ray, tmin, tmax);
        if (right == null) {
            return leftHit;
        }
        Hit rightHit = right.intersect(ray, tmin, leftHit == null ? tmax : leftHit.t());
        return (rightHit == null) ? leftHit : rightHit;
    }

    public static HittableVolume from(Hittable hittable) {
        if (hittable instanceof HittableVolume) {
            return (HittableVolume) hittable;
        }
        return new HittableVolume(hittable);
    }

    public static HittableVolume from(List<? extends Hittable> hittables) {

        if (hittables.size() == 0) {
            throw new IllegalArgumentException("empty list is not allowed");
        }
        if (hittables.size() == 1) {
            return from(hittables.get(0));
        }

        Axis axis = Axis.randomAxis();
        hittables.sort(Comparator.comparingDouble(h -> h.getBounds().min().component(axis)));

        if (hittables.size() == 2) {
            return new HittableVolume(hittables.get(0), hittables.get(1));
        }

        int split = hittables.size() / 2;
        List<? extends Hittable> left = new ArrayList<>(hittables.subList(0, split));
        List<? extends Hittable> right = new ArrayList<>(hittables.subList(split, hittables.size()));
        return new HittableVolume(from(left), from(right));
    }
}
