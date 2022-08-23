package com.epeters.raytrace.hitting;

import com.epeters.raytrace.solids.Ray;
import com.epeters.raytrace.utils.Vector;

import java.util.List;

import static com.epeters.raytrace.utils.Vector.vec;

public record BoundingBox(Vector min, Vector max) {

    public boolean hit(Ray ray, double tmin, double tmax) {

        Vector ro = ray.origin();
        Vector rd = ray.direction();

        for (int i=0; i<2; i++) {
            double id = 1.0f / rd.component(i);
            double t0 = (min.component(i) - ro.component(i)) * id;
            double t1 = (max.component(i) - ro.component(i)) * id;
            if (id < 0.0) {
                double tmp = t0;
                t0 = t1;
                t1 = tmp;
            }
            tmin = t0 > tmin ? t0 : tmin;
            tmax = t1 < tmax ? t1 : tmax;
            if (tmax <= tmin) {
                return false;
            }
        }
        return true;
    }

    public static BoundingBox combine(BoundingBox l, BoundingBox r) {
        double minX = Math.min(l.min().x(), r.min().x());
        double minY = Math.min(l.min().y(), r.min().y());
        double minZ = Math.min(l.min().z(), r.min().z());
        double maxX = Math.max(l.max().x(), r.max().x());
        double maxY = Math.max(l.max().y(), r.max().y());
        double maxZ = Math.max(l.max().z(), r.max().z());
        return new BoundingBox(vec(minX, minY, minZ), vec(maxX, maxY, maxZ));
    }

    public static BoundingBox combine(List<BoundingBox> boxes) {
        BoundingBox current = boxes.get(0);
        for (BoundingBox next : boxes.subList(1, boxes.size())) {
            current = combine(current, next);
        }
        return current;
    }
}
