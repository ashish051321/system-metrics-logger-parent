package com.yourorg.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemMetricsLogger {

    private final MeterRegistry meterRegistry;
    private final ConnectionPoolMetrics connectionPoolMetrics;

    public SystemMetricsLogger(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.connectionPoolMetrics = new ConnectionPoolMetrics(meterRegistry);
    }

    @Scheduled(fixedRate = 60000)
    public void logMetrics() {
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

            // Get Connection Pool Metrics
            String connectionPoolMetricsStr = connectionPoolMetrics.getConnectionPoolMetrics();

            // Splunk-friendly format with clear key-value pairs
            System.out.printf("METRICS|CPU_USAGE_PERCENT=%.2f|MEMORY_USED_MB=%s|MEMORY_AVAILABLE_MB=%s|MEMORY_USAGE_PERCENT=%.2f|THREAD_COUNT=%.0f|THREAD_USAGE_PERCENT=%.2f%s%n",
                    cpuUsage,
                    MetricUtil.formatBytes(memoryUsed),
                    MetricUtil.formatBytes(memoryAvailable),
                    memoryUsagePercent,
                    threadCount,
                    threadUsagePercent,
                    connectionPoolMetricsStr);
        } catch (Exception e) {
            System.out.println("ERROR|METRICS_COLLECTION_FAILED|" + e.getMessage());
        }
    }
}