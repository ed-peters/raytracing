package com.epeters.raytrace;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Helpers for common simple stuff
 */
public class Utils {

    public static final Vector RED = new Vector(1.0f, 0.0f, 0.0f);
    public static final Vector BLACK = Vector.ORIGIN;
    public static final Vector WHITE = new Vector(1.0f, 1.0f, 1.0f);
    public static final Vector GREEN = new Vector(0.0f, 1.0f, 0.0f);
    public static final Vector BLUE = new Vector(0.0f, 0.0f, 1.0f);
    public static final Vector SKY_BLUE = new Vector(0.5f, 0.7f, 1.0f);

    private static final Random rand = new Random();

    public static float random(float min, float max) {
        return min + (max - min) * rand.nextFloat();
    }

    public static Vector randomVector(float min, float max) {
        return new Vector(random(min, max), random(min, max), random(min, max));
    }

    public static float clamp(float x, float min, float max) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }

    public static int scaleInt(float x, int factor) {
        return (int)(clamp(x, 0.0f, 1.0f) * factor);
    }

    public static float sqrt(float f) {
        return (float) java.lang.Math.sqrt(f);
    }

    public static void time(Runnable runnable) {
        double start = System.currentTimeMillis();
        runnable.run();
        double secs = (System.currentTimeMillis() - start) / 1000.0;
        System.err.println(String.format("finished in %.3fs", secs));
    }

    public static <T> T time(Callable<T> callable) {
        double start = System.currentTimeMillis();
        try {
            T result = callable.call();
            double secs = (System.currentTimeMillis() - start) / 1000.0;
            System.err.println(String.format("finished in %.3fs", secs));
            return result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Ref<T> extends AtomicReference<T> {

    }
}
