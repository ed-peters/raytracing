package com.epeters.raytrace;

import com.epeters.raytrace.hittables.HitDetails;
import com.epeters.raytrace.solids.Solid;
import com.epeters.raytrace.utils.Vector;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.epeters.raytrace.utils.Utils.SKY_BLUE;
import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;

public class SceneConfig extends ArrayList<Solid> {

    public Vector position = ORIGIN;
    public Vector target = vec(0.0, 0.0, -1.0);
    public Vector up = vec(0.0, 1.0, 0.0);
    public double aspectRatio = 16.0 / 9.0;
    public double fieldOfView = 90.0;
    public double aperture = 0.0;
    public int imageWidth = 600;
    public int samplesPerPixel = 100;
    public int bouncesPerPixel = 10;
    public boolean useBoundingVolume = false;
    public Function<HitDetails,Vector> defaultColor = (h) -> h.normal().plus(WHITE).mul(0.5);
    public Function<Ray,Vector> backgroundColor = (r) -> {
        double t = 0.5f * (r.direction().normalize().y() + 1.0);
        return WHITE.mul(1.0 - t).plus(SKY_BLUE.mul(t));
    };
}
