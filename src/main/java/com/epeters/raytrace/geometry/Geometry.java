package com.epeters.raytrace.geometry;

import com.epeters.raytrace.Hit;
import com.epeters.raytrace.Ray;
import com.epeters.raytrace.Solid;
import com.epeters.raytrace.Vector;

import java.util.List;

/**
 * Interface for an object that knows how to determine whether it's hit by
 * a particular ray.
 */
public interface Geometry {

    Vector computeNormal(Vector point);

    double hit(Ray ray, double tmin, double tmax);

}