package com.yourorg.metrics;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@EnableScheduling
@ConditionalOnProperty(name = "system.metrics.enabled", havingValue = "true", matchIfMissing = true)
public class MetricsLoggerAutoConfiguration {

    @Bean
    public SystemMetricsLogger systemMetricsLogger(io.micrometer.core.instrument.MeterRegistry meterRegistry) {
        return new SystemMetricsLogger(meterRegistry);
    }
}