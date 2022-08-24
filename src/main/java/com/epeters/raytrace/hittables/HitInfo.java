package com.epeters.raytrace.hittables;

import com.epeters.raytrace.Ray;
import com.epeters.raytrace.solids.Solid;
import com.epeters.raytrace.Vector;

public class HitInfo {

    private final Ray ray;
    private final Solid solid;
    private final Vector point;
    private final Vector normal;
    private final boolean front;
    private Vector color;
    private Vector bounce;

    public HitInfo(Ray ray, Solid solid, Vector point, Vector normal) {
        this.ray = ray;
        this.solid = solid;
        this.point = point;
        if (ray.direction().dot(normal) < 0) {
            this.front = true;
            this.normal = normal.normalize();
        } else {
            this.front = false;
            this.normal = normal.negate().normalize();
        }
    }

    public Ray getRay() {
        return ray;
    }

    public Solid getSolid() {
        return solid;
    }

    public Vector getPoint() {
        return point;
    }

    public Vector getNormal() {
        return normal;
    }

    public boolean isFront() {
        return front;
    }

    public Vector getColor() {
        return color;
    }

    public Vector getBounce() {
        return bounce;
    }

    public void setColor(Vector color) {
        this.color = color;
    }

    public void setBounce(Vector bounce) {
        this.bounce = bounce;
    }
}
