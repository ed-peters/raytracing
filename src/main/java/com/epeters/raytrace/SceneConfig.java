package com.epeters.raytrace;

import com.epeters.raytrace.hittables.Hittable;
import com.epeters.raytrace.utils.Color;
import com.epeters.raytrace.utils.Vector;

import java.util.ArrayList;
import java.util.function.Function;

import static com.epeters.raytrace.utils.Color.RED;
import static com.epeters.raytrace.utils.Color.SKY_BLUE;
import static com.epeters.raytrace.utils.Color.WHITE;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;

public class SceneConfig extends ArrayList<Hittable> {

    public Vector position = ORIGIN;
    public Vector target = vec(0.0, 0.0, -1.0);
    public Vector up = vec(0.0, 1.0, 0.0);
    public double aspectRatio = 16.0 / 9.0;
    public double fieldOfView = 90.0;
    public double aperture = 0.0;
    public int imageWidth = 600;
    public int samplesPerPixel = 100;
    public int bouncesPerPixel = 10;
    public Color defaultColor = RED;
    public Hittable light = null;
    public Function<Ray, Color> backgroundColor = (r) -> {
        double t = 0.5f * (r.direction().normalize().y() + 1.0);
        return WHITE.mul(1.0 - t).plus(SKY_BLUE.mul(t));
    };
}
