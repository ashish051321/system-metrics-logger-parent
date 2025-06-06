package com.yourorg.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemMetricsLogger {

    private final MeterRegistry meterRegistry;

    public SystemMetricsLogger(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        System.out.println("SystemMetricsLogger initialized with meterRegistry: " + meterRegistry);
    }

    @Scheduled(fixedRate = 60000)
    public void logMetrics() {
        System.out.println("logMetrics method called");
        try {
            // CPU Usage (percentage)
            double cpuUsage = meterRegistry.get("system.cpu.usage").gauge().value() * 100;
            
            // Memory Usage
            double memoryUsed = meterRegistry.get("jvm.memory.used").gauge().value();
            double memoryMax = meterRegistry.get("jvm.memory.max").gauge().value();
            double memoryUsagePercent = (memoryUsed / memoryMax) * 100;
            double memoryAvailable = memoryMax - memoryUsed;
            
            // Thread Usage
            double threadCount = meterRegistry.get("jvm.threads.live").gauge().value();
            double threadPeak = meterRegistry.get("jvm.threads.peak").gauge().value();
            double threadUsagePercent = (threadCount / threadPeak) * 100;

            // Splunk-friendly format with clear key-value pairs
            System.out.printf("METRICS|CPU_USAGE_PERCENT=%.2f|MEMORY_USED_MB=%s|MEMORY_AVAILABLE_MB=%s|MEMORY_USAGE_PERCENT=%.2f|THREAD_COUNT=%.0f|THREAD_USAGE_PERCENT=%.2f%n",
                    cpuUsage,
                    MetricUtil.formatBytes(memoryUsed),
                    MetricUtil.formatBytes(memoryAvailable),
                    memoryUsagePercent,
                    threadCount,
                    threadUsagePercent);
        } catch (Exception e) {
            System.out.println("ERROR|METRICS_COLLECTION_FAILED|" + e.getMessage());
        }
    }
}