package com.epeters.raytrace.hitting;

import com.epeters.raytrace.solids.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.epeters.raytrace.utils.Utils.randomAxis;

public record BoundingVolume(Hittable left, Hittable right, BoundingBox boundingBox) implements Hittable {

    public static final Comparator<Hittable> COMPARE_X = Comparator.comparingDouble(h -> h.getBoundingBox().min().x());
    public static final Comparator<Hittable> COMPARE_Y = Comparator.comparingDouble(h -> h.getBoundingBox().min().y());
    public static final Comparator<Hittable> COMPARE_Z = Comparator.comparingDouble(h -> h.getBoundingBox().min().z());

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public Hit computeHit(Ray ray, double tmin, double tmax) {

        if (!getBoundingBox().hit(ray, tmin, tmax)) {
            return null;
        }

        Hit leftHit = left.computeHit(ray, tmin, tmax);
        if (left == right) {
            return leftHit;
        }

        // WTF he computes both but then returns the left hit first
        Hit rightHit = right.computeHit(ray, tmin, leftHit == null ? tmax : leftHit.t());
        return rightHit == null ? leftHit : rightHit;
        // return leftHit == null ? rightHit : leftHit;
    }

    public static BoundingVolume from(Hittable only) {
        return new BoundingVolume(only, only, only.getBoundingBox());
    }

    public static BoundingVolume from(Hittable left, Hittable right) {
        return new BoundingVolume(left, right, BoundingBox.combine(left.getBoundingBox(), right.getBoundingBox()));
    }

    public static BoundingVolume from(List<? extends Hittable> hittables) {

        // only one object? simple!
        if (hittables.size() == 1) {
            return from(hittables.get(0));
        }

        // more than one? sort on a random axis moving downward
        Collections.sort(hittables, switch (randomAxis()) {
            case 0 -> COMPARE_X;
            case 1 -> COMPARE_Y;
            case 2 -> COMPARE_Z;
            default -> throw new IllegalArgumentException("unknown axis for comparison");
        });

        // two objects? simple!
        if (hittables.size() == 2) {
            return from(hittables.get(0), hittables.get(1));
        }

        int split = hittables.size() / 2;
        BoundingVolume left = from(new ArrayList<>(hittables.subList(0, split)));
        BoundingVolume right = from(new ArrayList<>(hittables.subList(split, hittables.size())));
        return from(from(left), from(right));
    }
}
