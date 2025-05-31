package com.yourorg.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemMetricsLogger {

    private final MeterRegistry meterRegistry;

    public SystemMetricsLogger(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedRate = 60000)
    public void logMetrics() {
        double cpu = meterRegistry.get("system.cpu.usage").gauge().value();
        double memory = meterRegistry.get("jvm.memory.used").gauge().value();
        double threads = meterRegistry.get("jvm.threads.live").gauge().value();
        System.out.printf("[Metrics] CPU: %.2f%% | Memory: %s | Threads: %.0f%n",
                cpu * 100, MetricUtil.formatBytes(memory), threads);
    }
}