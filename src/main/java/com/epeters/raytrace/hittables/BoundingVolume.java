package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.epeters.raytrace.utils.Utils.randomComponent;

/**
 * Implementation of {@link Hittable} that represents an abstract volume in space. Implements a
 * recursive tree structure that speeds up intersection testing.
 *
 * @see <a href="https://raytracing.github.io/books/RayTracingTheNextWeek.html#boundingvolumehierarchies/thebvhnodeclass">guide</a>
 */
public class BoundingVolume implements Hittable {

    private final Hittable left;
    private final Hittable right;
    private final BoundingBox bounds;

    public BoundingVolume(Hittable left) {
        this.left = left;
        this.right = null;
        this.bounds = left.getBounds();
    }

    public BoundingVolume(Hittable left, Hittable right) {
        this.left = left;
        this.right = right;
        this.bounds = right != null
                ? new BoundingBox(Arrays.asList(left.getBounds(), right.getBounds()))
                : left.getBounds();
    }

    @Override
    public BoundingBox getBounds() {
        return bounds;
    }

    @Override
    public Hit hit(Ray ray, double tmin, double tmax) {

        if (!getBounds().hit(ray, tmin, tmax)) {
            return null;
        }

        Hit leftHit = left.hit(ray, tmin, tmax);
        if (right == null) {
            return leftHit;
        }

        Hit rightHit = right.hit(ray, tmin, leftHit == null ? tmax : leftHit.t());
        return (rightHit == null) ? leftHit : rightHit;
    }

    public static BoundingVolume from(List<? extends Hittable> hittables) {

        if (hittables.size() == 1) {
            return new BoundingVolume(hittables.get(0));
        }

        final int c = randomComponent();
        Collections.sort(hittables, Comparator.comparingDouble(h -> h.getBounds().getMin().component(c)));

        if (hittables.size() == 2) {
            return new BoundingVolume(hittables.get(0), hittables.get(1));
        }

        int split = hittables.size() / 2;
        List<? extends Hittable> left = new ArrayList<>(hittables.subList(0, split));
        List<? extends Hittable> right = new ArrayList<>(hittables.subList(split, hittables.size()));
        return new BoundingVolume(from(left), from(right));
    }
}
