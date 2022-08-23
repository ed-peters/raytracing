package com.epeters.raytrace.rendering;

import com.epeters.raytrace.solids.Solid;
import com.epeters.raytrace.utils.Vector;

import java.util.ArrayList;

import static com.epeters.raytrace.utils.Vector.ORIGIN;
import static com.epeters.raytrace.utils.Vector.vec;
import static java.lang.Math.tan;
import static java.lang.Math.toRadians;

public class RendererConfig extends ArrayList<Solid> {

    // =====================================================
    // camera pose
    // =====================================================

    public Vector position = ORIGIN;
    public Vector target = vec(0.0, 0.0, -1.0);
    public Vector up = vec(0.0, 1.0, 0.0);

    // =====================================================
    // viewport parameters
    // =====================================================

    public double aspectRatio = 16.0 / 9.0;
    public double fieldOfView = 90.0;
    public double focalDistance = 1.0;
    public double aperture = 0.0;

    // =====================================================
    // image quality and render performance preferences
    // =====================================================

    public int samplesPerPixel = 100;
    public int bouncesPerPixel = 10;
    public int imageWidth = 400;
    public int threads = 0;
    public boolean boundingVolume = true;

    // =====================================================
    // computed properties
    // =====================================================

    public double viewportWidth;
    public double viewportHeight;
    public int imageHeight;

    // =====================================================
    // motion blur
    // =====================================================

    public boolean motionBlur = false;

    /**
     * Initalizes computed values
     */
    public RendererConfig init() {
        this.imageHeight = (int)(imageWidth / aspectRatio);
        this.focalDistance = position.minus(target).length();
        this.viewportHeight = 2.0 * tan(toRadians(fieldOfView) / 2.0);
        this.viewportWidth = aspectRatio * viewportHeight;
        return this;
    }
}
