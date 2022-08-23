package com.epeters.raytrace.rendering;

import com.epeters.raytrace.utils.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Renderer {

    public static final int CHUNK_SIZE = 20;

    private final Tracer tracer;
    private final int imageWidth;
    private final int imageHeight;
    private final ParallelProgress progress;

    public Renderer(RendererConfig config) {
        this.tracer = new Tracer(config);
        this.imageWidth = config.imageWidth;
        this.imageHeight = config.imageHeight;
        this.progress = new ParallelProgress(imageHeight * imageWidth);
    }

    /**
     * Renders a single row of the image
     * @param row the row to be rendered (0 is the top row of the image)
     * @return pixel data (left to right)
     */
    private Vector [] renderRow(int row) {

        // y coordinates have 0 at the bottom
        int y = imageHeight - row - 1;
        if (y < 0) {
            throw new IllegalArgumentException("illegal row "+row);
        }

        Vector [] pixels = new Vector[imageWidth];
        for (int x=0; x<imageWidth; x++) {
            pixels[x] = tracer.renderPixel(x, y);
            progress.pixelsComplete(1);
        }
        return pixels;
    }

    /**
     * Render the full image, in multiple threads if necessary
     * @return pixel data (from top to bottom, left to right)
     */
    public List<Vector[]> render() {

        if (tracer.getConfig().threads == 0) {
            return renderRows(0, imageHeight);
        }

        progress.start();

        Map<Integer,Vector[]> rows = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(tracer.getConfig().threads);
        for (int row=0; row<imageHeight; row++) {
            final int thisRow = row;
            executor.submit(() -> {
                rows.put(thisRow, renderRow(thisRow));
            });
        };
        executor.shutdown();

        try {
            while (!executor.isTerminated()) {
                Thread.sleep(2000L);
                progress.reportProgress();
            }
            progress.reportProgress();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new TreeSet<>(rows.keySet()).stream()
                .map(k -> rows.get(k))
                .collect(Collectors.toList());
    }

    /**
     * Renders the specified number of rows in the calling thread.
     *
     * @param firstRowInclusive the first row to render
     * @param lastRowExclusive the index one greater than the last row to render
     * @return pixel data (from top to bottom, left to right)
     */
    private List<Vector[]> renderRows(int firstRowInclusive, int lastRowExclusive) {
        List<Vector[]> result = new ArrayList<>(lastRowExclusive - firstRowInclusive);
        for (int y=lastRowExclusive-1; y>=firstRowInclusive; y--) {
            Vector [] row = new Vector[imageWidth];
            for (int x=0; x<imageWidth; x++) {
                row[x] = tracer.renderPixel(x, y);
            }
            result.add(row);
        }
        return result;
    }
}
