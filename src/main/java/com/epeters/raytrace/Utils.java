package com.epeters.raytrace;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Helpers for common simple stuff
 */
public class Utils {

    public static final Vector RED = new Vector(1.0, 0.0, 0.0);
    public static final Vector BLACK = Vector.ORIGIN;
    public static final Vector WHITE = new Vector(1.0, 1.0, 1.0);
    public static final Vector GREEN = new Vector(0.0, 1.0, 0.0);
    public static final Vector BLUE = new Vector(0.0, 0.0, 1.0);
    public static final Vector SKY_BLUE = new Vector(0.5, 0.7, 1.0);
    public static final Vector MID_GRAY = new Vector(0.5, 0.5, 0.5);

    private static final Random rand = new Random();

    public static double random(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    public static Vector randomVector(double min, double max) {
        return new Vector(random(min, max), random(min, max), random(min, max));
    }

    public static double clamp(double x, double min, double max) {
        if (x < min) {
            return min;
        }
        return Math.min(x, max);
    }

    public static int scaleInt(double val, int factor) {
        return (int)(clamp(val, 0.0, 1.0) * factor);
    }

    public static double sqrt(double val) {
        return java.lang.Math.sqrt(val);
    }

    public static void time(Runnable runnable) {
        double start = System.currentTimeMillis();
        runnable.run();
        double secs = (System.currentTimeMillis() - start) / 1000.0;
        System.err.printf("finished in %.3fs%n", secs);
    }

    public static <T> T time(Callable<T> callable) {
        double start = System.currentTimeMillis();
        try {
            T result = callable.call();
            double secs = (System.currentTimeMillis() - start) / 1000.0;
            System.err.printf("finished in %.3fs%n", secs);
            return result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Ref<T> extends AtomicReference<T> {

    }
}
