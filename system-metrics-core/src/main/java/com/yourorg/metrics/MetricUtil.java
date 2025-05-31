package com.yourorg.metrics;

public class MetricUtil {
    public static String formatBytes(double bytes) {
        return String.format("%.2f MB", bytes / (1024 * 1024));
    }
}