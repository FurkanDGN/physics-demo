package me.furkandgn.physicsdemo.common.util;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PerformanceTracker {

  private final Map<MetricType, MetricData> metrics = new EnumMap<>(MetricType.class);
  private final MetricData frameData = new MetricData();
  public PerformanceTracker() {
    for (MetricType type : MetricType.values()) {
      this.metrics.put(type, new MetricData());
    }
  }

  public void countFrame() {
    long currentTime = System.currentTimeMillis();
    this.frameData.timestamps.add(currentTime);
    this.cleanUpOldEntries(this.frameData, currentTime);
  }

  private void cleanUpOldEntries(MetricData data, long currentTime) {
    while (!data.timestamps.isEmpty() && data.timestamps.peek() < currentTime - 1000) {
      data.timestamps.remove();
      if (!data.values.isEmpty()) {
        data.values.remove();
      }
    }
  }

  public void recordMetric(MetricType type, long value) {
    long currentTime = System.currentTimeMillis();
    MetricData data = this.metrics.get(type);
    data.timestamps.add(currentTime);
    data.values.add(value);
    this.cleanUpOldEntries(data, currentTime);
  }

  public double getAverage(MetricType type) {
    MetricData data = this.metrics.get(type);
    if (data.values.isEmpty()) {
      return 0;
    }
    return data.values.stream().mapToLong(Long::longValue).average().orElse(0) / TimeUtil.MILLISECOND_TO_NANOS;
  }

  public int getFPS() {
    return this.frameData.timestamps.size();
  }

  private static class MetricData {
    Queue<Long> timestamps = new LinkedList<>();
    Queue<Long> values = new LinkedList<>();
  }
}