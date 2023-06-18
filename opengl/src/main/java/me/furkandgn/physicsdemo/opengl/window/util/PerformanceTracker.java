package me.furkandgn.physicsdemo.opengl.window.util;

import me.furkandgn.physicsdemo.common.util.TimeUtil;

public class PerformanceTracker {

    private long firstRender = 0;
    private short frameCount = 0;
    private double tickTime = 0;
    private double renderTime = 0;

    public void countFrame() {
        if (this.firstRender == 0) {
            this.firstRender = System.currentTimeMillis();
        } else if (this.firstRender <= System.currentTimeMillis() - 1000) {
            System.out.printf("FPS: %s    -    total tick time: %.2f (%.2f) \t total render time: %.2f (%.2f)%n", this.frameCount, this.tickTime, this.tickTime / this.frameCount, this.renderTime, this.renderTime / this.frameCount);
            this.firstRender = System.currentTimeMillis();
            this.frameCount = 0;
            this.tickTime = 0;
            this.renderTime = 0;
        }
        this.frameCount++;
    }

    public void recordTickTime(long duration) {
        this.tickTime += duration / TimeUtil.MILLISECOND_TO_NANOS;
    }

    public void recordRenderTime(long duration) {
        this.renderTime += duration / TimeUtil.MILLISECOND_TO_NANOS;
    }
}