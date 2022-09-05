package com.epeters.raytrace.renderer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements progress reporting for rendering
 */
public final class ParallelProgress {

    private final int pixelsTotal;
    private double startTime;
    private double lastIntervalTime;
    private AtomicInteger pixelsLastInterval;
    private int pixelsComplete;

    public ParallelProgress(int pixelsTotal) {
        this.pixelsTotal = pixelsTotal;
        this.startTime = 0.0;
        this.lastIntervalTime = 0.0;
        this.pixelsLastInterval = new AtomicInteger(0);
        this.pixelsComplete = 0;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        lastIntervalTime = startTime;
        pixelsLastInterval.set(0);
        pixelsComplete = 0;
    }

    public void pixelsComplete(int count) {
        pixelsLastInterval.addAndGet(count);
    }

    public void reportProgress() {

        double currentTime = System.currentTimeMillis();
        double thisIntervalTime = currentTime - lastIntervalTime;
        double totalTime = currentTime - startTime;
        lastIntervalTime = currentTime;

        int pixelsThisInterval = pixelsLastInterval.getAndSet(0);
        pixelsComplete += pixelsThisInterval;

        double freeMemory = Runtime.getRuntime().freeMemory();
        freeMemory /= Runtime.getRuntime().totalMemory();
        freeMemory *= 100.0;

        int pctComplete = (int)(pixelsComplete * 100.0 / pixelsTotal);
        double pixelsPerMinute = pixelsThisInterval / (thisIntervalTime / 60000.0);
        double pixelsPerMinuteAvg = pixelsComplete / (totalTime / 60000.0);
        double minutesLeft = (pixelsTotal - pixelsComplete) / pixelsPerMinuteAvg;
        System.err.printf("%d pixels (%d%%) complete at %.0f pixels/min -> %.3f min remaining (%.3f%% free memory)%n",
                pixelsComplete,
                pctComplete,
                pixelsPerMinute,
                minutesLeft,
                freeMemory);
    }
}
