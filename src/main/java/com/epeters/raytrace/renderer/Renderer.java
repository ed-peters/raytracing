package com.epeters.raytrace.renderer;

import com.epeters.raytrace.Tracer;
import com.epeters.raytrace.utils.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class Renderer {

    private final Tracer tracer;
    private final File file;
    private final int threads;
    private final ParallelProgress progress;

    public Renderer(Tracer tracer, String path, int threads) {
        this.tracer = tracer;
        this.file = new File(path);
        this.threads = threads;
        this.progress = new ParallelProgress(tracer.getImageWidth() * tracer.getImageHeight());
    }

    public void render() {
        List<Color[]> rows = threads > 1 ? renderThreaded() : renderImmediate();
        writeData(rows);
    }

    public List<Color[]> renderImmediate() {
        List<Color[]> rows = new ArrayList<>(tracer.getImageWidth());
        for (int y=tracer.getImageHeight()-1; y>=0; y--) {
            rows.add(renderRow(y));
        }
        return rows;
    }

    public List<Color[]> renderThreaded() {

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Map<Integer,Color[]> rows = new ConcurrentHashMap<>();

        progress.start();
        for (int i=0; i<tracer.getImageHeight(); i++) {
            final int thisRow = i;
            executor.submit(() -> {
                rows.put(thisRow, renderRow(thisRow));
            });
        }
        executor.shutdown();

        try {
            while (!executor.awaitTermination(2000L, TimeUnit.MILLISECONDS)) {
                progress.reportProgress();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        progress.reportProgress();

        List<Integer> list = new ArrayList<>(rows.keySet());
        list.sort(Comparator.reverseOrder());
        return list.stream().map(rows::get).collect(Collectors.toList());
    }

    /** Renders a single row of the image */
    public Color[] renderRow(int y) {
        Color [] row = new Color[tracer.getImageWidth()];
        for (int x=0; x<tracer.getImageWidth(); x++) {
            row[x] = tracer.renderPixel(x, y);
            progress.pixelsComplete(1);
        }
        return row;
    }

    public void writeData(List<Color[]> rows) {
        BufferedImage image = new BufferedImage(tracer.getImageWidth(), tracer.getImageHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y=0; y<rows.size(); y++) {
            Color [] row = rows.get(y);
            for (int x=0; x<row.length; x++) {
                Color pixel = row[x];
                int rgb = pixel.toRgb();
                image.setRGB(x, y, rgb);
            }
        }
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
