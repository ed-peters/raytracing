package com.epeters.raytrace.utils;

import static com.epeters.raytrace.utils.Axis.X;
import static com.epeters.raytrace.utils.Axis.Y;
import static com.epeters.raytrace.utils.Axis.Z;
import static com.epeters.raytrace.utils.Vector.vec;

public enum XYZPlane {

    XY(X, Y, Z, vec(0.0, 0.0, 1.0)),
    YZ(Y, Z, X, vec(1.0, 0.0, 0.0)),
    XZ(X, Z, Y, vec(0.0, 1.0, 0.0));

    public final Axis i;
    public final Axis j;
    public final Axis k;
    public final Vector normal;

    XYZPlane(Axis i, Axis j, Axis k, Vector normal) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.normal = normal;
    }
}
