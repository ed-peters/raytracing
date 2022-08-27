package com.epeters.raytrace;

import com.epeters.raytrace.renderer.Renderer;
import com.epeters.raytrace.utils.Utils;

public class Main {

    public static void main(String [] args) {

        SceneConfig config = Scenes.redBox();
        config.imageWidth = 1400;
        config.samplesPerPixel = 500;
        config.bouncesPerPixel = 50;

        int threads = Runtime.getRuntime().availableProcessors() - 1;
        String path = System.getProperty("user.home") + "/Desktop/trace.png";

        Tracer tracer = new Tracer(config);
        Renderer renderer = new Renderer(tracer, path, threads);
        Utils.time(() -> renderer.render());
    }
}
