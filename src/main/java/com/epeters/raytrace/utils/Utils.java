package com.epeters.raytrace.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import static com.epeters.raytrace.utils.Vector.vec;

/**
 * Helpers for common simple stuff
 */
public final class Utils {

    public static final Vector RED = vec(1.0, 0.0, 0.0);
    public static final Vector BLACK = Vector.ORIGIN;
    public static final Vector WHITE = vec(1.0, 1.0, 1.0);
    public static final Vector GREEN = vec(0.0, 1.0, 0.0);
    public static final Vector BLUE = vec(0.0, 0.0, 1.0);
    public static final Vector SKY_BLUE = vec(0.5, 0.7, 1.0);
    public static final Vector MID_GRAY = vec(0.5, 0.5, 0.5);
    public static final Vector DARK_PINK = vec(0.8, 0.0, 0.4);
    public static final Vector DARK_GREEN = vec(0.0, 0.4, 0.0);

    public static double random() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double random(double min, double max) {
        return min + (max - min) * random();
    }

    public static Vector randomVector(double min, double max) {
        return vec(random(min, max), random(min, max), random(min, max));
    }

    public static int randomInt(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }

    public static int randomComponent() {
        return randomInt(3);
    }

    public static Vector randomVectorInUnitCube() {
        return randomVector(-1.0, 1.0);
    }

    public static Vector randomUnitVector() {
        while (true) {
            Vector next = randomVectorInUnitCube();
            if (next.square() < 1.0) {
                return next.normalize();
            }
        }
    }

    public static Vector randomVectorInUnitDisc() {
        while (true) {
            Vector next = vec(random(-1.0, 1.0), random(-1.0, 1.0), 0.0);
            if (next.square() < 1.0) {
                return next;
            }
        }
    }

    public static double clamp(double x, double min, double max) {
        if (x < min) {
            return min;
        }
        return Math.min(x, max);
    }

    public static double dot(double ax, double ay, double az) {
        return dot(ax, ay, az, ax, ay, az);
    }

    public static double dot(double ax, double ay, double az, double bx, double by, double bz) {
        return ax * bx + ay * by + az * bz;
    }

    public static int scaleInt(double val, int factor) {
        return (int)(clamp(val, 0.0, 1.0) * factor);
    }

    public static double sqrt(double val) {
        return java.lang.Math.sqrt(val);
    }

    public static double square(double val) {
        return val * val;
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
}
