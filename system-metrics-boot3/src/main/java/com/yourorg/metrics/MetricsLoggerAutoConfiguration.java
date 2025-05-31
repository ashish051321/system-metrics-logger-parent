package com.yourorg.metrics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(name = "system.metrics.enabled", havingValue = "true", matchIfMissing = true)
@Import(SystemMetricsLogger.class)
public class MetricsLoggerAutoConfiguration {
}