package com.epeters.raytrace.utils;

import java.util.concurrent.ThreadLocalRandom;

public enum Axis {

    X,
    Y,
    Z;

    public static Axis randomAxis() {
        Axis [] values = values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
