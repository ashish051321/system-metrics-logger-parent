package com.yourorg.metrics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "system.metrics.enabled", havingValue = "true", matchIfMissing = true)
@Import(SystemMetricsLogger.class)
public class MetricsLoggerAutoConfiguration {
}