package com.epeters.raytrace;

import static com.epeters.raytrace.Vector.ORIGIN;
import static com.epeters.raytrace.Vector.vec;

public class CameraSettings {

    public Vector position = ORIGIN;
    public Vector target = vec(0.0, 0.0, -1.0);
    public Vector up = vec(0.0, 1.0, 0.0);
    public double aspectRatio = 16.0 / 9.0;
    public double fieldOfView = 90.0;
    public double focalDistance = 1.0;
    public double aperture = 0.0;

}
