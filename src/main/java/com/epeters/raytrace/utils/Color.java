package com.epeters.raytrace.utils;

import static com.epeters.raytrace.utils.Utils.scaleInt;
import static java.lang.Math.sqrt;

public record Color(double r, double g, double b) {

    public static final Color RED = color(1.0, 0.0, 0.0);
    public static final Color BLACK = color(0.0, 0.0, 0.0);
    public static final Color WHITE = color(1.0, 1.0, 1.0);
    public static final Color GREEN = color(0.0, 1.0, 0.0);
    public static final Color BLUE = color(0.0, 0.0, 1.0);
    public static final Color SKY_BLUE = color(0.5, 0.7, 1.0);
    public static final Color MID_GRAY = color(0.5, 0.5, 0.5);
    public static final Color DARK_GREEN = color(0.0, 0.4, 0.0);

    public Color plus(Color other) {
        return color(r + other.r, g + other.g, b + other.b);
    }

    public Color mul(Color other) {
        return color(r * other.r, g * other.g, b * other.b);
    }

    public Color mul(double f) {
        return color(r * f, g * f, b * f);
    }

    public Color div(double f) {
        return mul(1.0 / f);
    }

    public Color normalize(double sampleCount) {
        return color(sqrt(r / sampleCount), sqrt(g / sampleCount), sqrt(b / sampleCount));
    }

    public int toRgb() {
        int r = scaleInt(clean(r()), 255);
        int g = scaleInt(clean(g()), 255);
        int b = scaleInt(clean(b()), 255);
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        return 0xFF000000 | r | g | b;
    }

    private double clean(double d) {
        return d; // Double.isNaN(d) ? 0.0 : d;
    }

    public static Color color(double x, double y, double z) {
        return new Color(x, y, z);
    }
}
