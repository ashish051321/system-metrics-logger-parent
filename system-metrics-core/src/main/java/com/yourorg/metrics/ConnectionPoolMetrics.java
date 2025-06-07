package com.yourorg.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import org.apache.tomcat.jdbc.pool.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPoolMetrics {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolMetrics.class);
    private final MeterRegistry meterRegistry;

    public ConnectionPoolMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public String getConnectionPoolMetrics() {
        StringBuilder metrics = new StringBuilder();

        // Check for Tomcat JDBC Pool
        try {
            // Tomcat metrics
            Gauge activeGauge = meterRegistry.find("tomcat.jdbc.connections.active").gauge();
            Gauge maxGauge = meterRegistry.find("tomcat.jdbc.connections.max").gauge();
            Gauge idleGauge = meterRegistry.find("tomcat.jdbc.connections.idle").gauge();

            if (activeGauge != null && maxGauge != null && idleGauge != null) {
                double activeConnections = activeGauge.value();
                double maxConnections = maxGauge.value();
                double idleConnections = idleGauge.value();
                double connectionUsagePercent = maxConnections > 0 ? (activeConnections / maxConnections) * 100 : 0;

                metrics.append(String.format("|TOMCAT_ACTIVE_CONNECTIONS=%.0f|TOMCAT_MAX_CONNECTIONS=%.0f|TOMCAT_IDLE_CONNECTIONS=%.0f|TOMCAT_CONNECTION_USAGE_PERCENT=%.2f",
                    activeConnections, maxConnections, idleConnections, connectionUsagePercent));
            }
        } catch (Exception e) {
            logger.debug("TOMCAT_POOL_NOT_FOUND: {}", e.getMessage());
        }

        // Check for HikariCP
        try {
            // HikariCP metrics
            Gauge activeGauge = meterRegistry.find("hikaricp.connections.active").gauge();
            Gauge maxGauge = meterRegistry.find("hikaricp.connections.max").gauge();
            Gauge idleGauge = meterRegistry.find("hikaricp.connections.idle").gauge();

            if (activeGauge != null && maxGauge != null && idleGauge != null) {
                double activeConnections = activeGauge.value();
                double maxConnections = maxGauge.value();
                double idleConnections = idleGauge.value();
                double connectionUsagePercent = maxConnections > 0 ? (activeConnections / maxConnections) * 100 : 0;

                metrics.append(String.format("|HIKARI_ACTIVE_CONNECTIONS=%.0f|HIKARI_MAX_CONNECTIONS=%.0f|HIKARI_IDLE_CONNECTIONS=%.0f|HIKARI_CONNECTION_USAGE_PERCENT=%.2f",
                    activeConnections, maxConnections, idleConnections, connectionUsagePercent));
            }
        } catch (Exception e) {
            logger.debug("HIKARI_POOL_NOT_FOUND: {}", e.getMessage());
        }

        return metrics.toString();
    }

    public void collectTomcatPoolMetrics() {
        try {
            // ... existing code ...
        } catch (Exception e) {
            logger.debug("TOMCAT_POOL_NOT_FOUND: {}", e.getMessage());
        }
    }

    public void collectHikariPoolMetrics() {
        try {
            // ... existing code ...
        } catch (Exception e) {
            logger.debug("HIKARI_POOL_NOT_FOUND: {}", e.getMessage());
        }
    }
} 