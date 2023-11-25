package me.furkandgn.physicsdemo.common.util;

import java.util.ArrayList;
import java.util.List;

public class PerformanceTracker {

  private long firstRender = 0;
  private final List<Long> frameCount = new ArrayList<>();
  private double tickTime = 0;
  private double renderTime = 0;
  private double preTime = 0;
  private double postTime = 0;
  private double frameTime = 0;

  public void countFrame() {
    if (this.firstRender == 0) {
      this.firstRender = System.nanoTime();
    }
    this.frameCount.add(System.currentTimeMillis());

    if (this.firstRender <= System.nanoTime() - 1e7) {
      this.frameCount.removeIf(timestamp -> timestamp < System.currentTimeMillis() - 1000);
      System.out.printf("\rFPS: %d    -    frame time: %.2f \t tick time: %.2f \t render time: %.2f \t pre time: %.2f \t post time: %.2f",
        this.frameCount.size(), this.frameTime, this.tickTime, this.renderTime, this.preTime, this.postTime);
      this.firstRender = System.nanoTime();
      this.frameTime = 0;
      this.tickTime = 0;
      this.renderTime = 0;
      this.preTime = 0;
      this.postTime = 0;
    }
  }

  public void recordTickTime(long duration) {
    this.tickTime = ((this.tickTime + duration) / 2) / TimeUtil.MILLISECOND_TO_NANOS;
  }

  public void recordRenderTime(long duration) {
    this.renderTime = ((this.renderTime + duration) / 2) / TimeUtil.MILLISECOND_TO_NANOS;
  }

  public void recordPreTime(long duration) {
    this.preTime = ((this.preTime + duration) / 2) / TimeUtil.MILLISECOND_TO_NANOS;
  }

  public void recordPostTime(long duration) {
    this.postTime = ((this.postTime + duration) / 2) / TimeUtil.MILLISECOND_TO_NANOS;
  }

  public void recordFrameTime(long duration) {
    this.frameTime = duration / TimeUtil.MILLISECOND_TO_NANOS;
  }
}