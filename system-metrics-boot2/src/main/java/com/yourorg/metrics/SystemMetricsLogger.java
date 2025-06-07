package com.yourorg.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemMetricsLogger {

    private static final Logger logger = LoggerFactory.getLogger(SystemMetricsLogger.class);
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
            logger.info("METRICS|CPU_USAGE_PERCENT={}|MEMORY_USED_MB={}|MEMORY_AVAILABLE_MB={}|MEMORY_USAGE_PERCENT={}|THREAD_COUNT={}|THREAD_USAGE_PERCENT={}{}",
                    String.format("%.2f", cpuUsage),
                    MetricUtil.formatBytes(memoryUsed),
                    MetricUtil.formatBytes(memoryAvailable),
                    String.format("%.2f", memoryUsagePercent),
                    threadCount,
                    String.format("%.2f", threadUsagePercent),
                    connectionPoolMetricsStr);
        } catch (Exception e) {
            logger.error("METRICS_COLLECTION_FAILED: {}", e.getMessage(), e);
        }
    }
}