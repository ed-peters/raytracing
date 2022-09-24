package com.epeters.raytrace.utils;

import static java.lang.Math.abs;
import static com.epeters.raytrace.utils.Vector.vec;

public record Basis(Vector u, Vector v, Vector w) {

    public Vector local(Vector a) {
        return u.mul(a.x()).plus(v.mul(a.y())).plus(w.mul(a.z()));
    }

    public static Basis fromW(Vector n) {
        Vector w = n.normalize();
        Vector a = abs(w.x()) > 0.9 ? vec(0.0, 1.0, 0.0) : vec(1.0, 0.0, 0.0);
        Vector v = w.cross(a).normalize();
        Vector u = w.cross(v).normalize();
        return new Basis(u, v, w);
    }
}
