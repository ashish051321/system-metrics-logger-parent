# System Metrics Logger

A Spring Boot library that provides system metrics logging functionality for both Spring Boot 2.x and 3.x applications.

## Features

- CPU usage monitoring
- Memory usage tracking
- Thread count monitoring
- Connection pool monitoring (Tomcat JDBC and HikariCP)
- Compatible with both Spring Boot 2.x and 3.x
- Auto-configuration support
- Configurable logging interval

## Metrics Description

The library collects and logs the following metrics in a Splunk-friendly format:

### CPU Metrics
- `CPU_USAGE_PERCENT`: Percentage of CPU being utilized by the JVM process
  - Range: 0-100%
  - Higher values indicate higher CPU load
  - Example: `CPU_USAGE_PERCENT=25.50` means 25.5% CPU utilization

### Memory Metrics
- `MEMORY_USED_MB`: Amount of memory currently being used by the JVM
  - Measured in megabytes (MB)
  - Example: `MEMORY_USED_MB=512MB` means 512MB of memory is in use

- `MEMORY_AVAILABLE_MB`: Amount of memory still available to the JVM
  - Measured in megabytes (MB)
  - Example: `MEMORY_AVAILABLE_MB=1536MB` means 1536MB is still available

- `MEMORY_USAGE_PERCENT`: Percentage of maximum memory being used
  - Range: 0-100%
  - Calculated as (used memory / max memory) * 100
  - Example: `MEMORY_USAGE_PERCENT=25.00` means 25% of max memory is used

### Thread Metrics
- `THREAD_COUNT`: Current number of live threads in the JVM
  - Integer value
  - Example: `THREAD_COUNT=45` means 45 threads are currently active

- `THREAD_USAGE_PERCENT`: Percentage of peak thread count being used
  - Range: 0-100%
  - Calculated as (current threads / peak threads) * 100
  - Example: `THREAD_USAGE_PERCENT=75.00` means using 75% of peak thread count

### Connection Pool Metrics

#### Tomcat JDBC Pool Metrics (if using Tomcat)
- `TOMCAT_ACTIVE_CONNECTIONS`: Number of active database connections
  - Integer value
  - Example: `TOMCAT_ACTIVE_CONNECTIONS=5` means 5 connections are currently in use

- `TOMCAT_MAX_CONNECTIONS`: Maximum number of connections allowed
  - Integer value
  - Example: `TOMCAT_MAX_CONNECTIONS=20` means pool can have up to 20 connections

- `TOMCAT_IDLE_CONNECTIONS`: Number of idle connections in the pool
  - Integer value
  - Example: `TOMCAT_IDLE_CONNECTIONS=10` means 10 connections are available

- `TOMCAT_CONNECTION_USAGE_PERCENT`: Percentage of max connections being used
  - Range: 0-100%
  - Example: `TOMCAT_CONNECTION_USAGE_PERCENT=25.00` means 25% of max connections are in use

#### HikariCP Metrics (if using HikariCP)
- `HIKARI_ACTIVE_CONNECTIONS`: Number of active database connections
  - Integer value
  - Example: `HIKARI_ACTIVE_CONNECTIONS=3` means 3 connections are currently in use

- `HIKARI_MAX_CONNECTIONS`: Maximum number of connections allowed
  - Integer value
  - Example: `HIKARI_MAX_CONNECTIONS=15` means pool can have up to 15 connections

- `HIKARI_IDLE_CONNECTIONS`: Number of idle connections in the pool
  - Integer value
  - Example: `HIKARI_IDLE_CONNECTIONS=8` means 8 connections are available

- `HIKARI_CONNECTION_USAGE_PERCENT`: Percentage of max connections being used
  - Range: 0-100%
  - Example: `HIKARI_CONNECTION_USAGE_PERCENT=20.00` means 20% of max connections are in use

### Output Format
The metrics are logged in a pipe-delimited format for easy parsing:
```
METRICS|CPU_USAGE_PERCENT=25.50|MEMORY_USED_MB=512MB|MEMORY_AVAILABLE_MB=1536MB|MEMORY_USAGE_PERCENT=25.00|THREAD_COUNT=45|THREAD_USAGE_PERCENT=75.00|TOMCAT_ACTIVE_CONNECTIONS=5|TOMCAT_MAX_CONNECTIONS=20|TOMCAT_IDLE_CONNECTIONS=10|TOMCAT_CONNECTION_USAGE_PERCENT=25.00
```

### Error and Debug Messages
- Error messages are logged when metrics collection fails:
  ```
  ERROR|METRICS_COLLECTION_FAILED|<error message>
  ```
- Debug messages are logged when connection pools are not found:
  ```
  DEBUG|TOMCAT_POOL_NOT_FOUND|<error message>
  DEBUG|HIKARI_POOL_NOT_FOUND|<error message>
  ```

## Modules

- `system-metrics-core`: Core functionality and utilities
- `system-metrics-boot2`: Spring Boot 2.x implementation
- `system-metrics-boot3`: Spring Boot 3.x implementation

## Requirements

### For Spring Boot 2.x
- Java 8 or higher
- Spring Boot 2.x
- Spring Boot Actuator
- Either Tomcat JDBC Pool or HikariCP (for connection pool metrics)

### For Spring Boot 3.x
- Java 17 or higher
- Spring Boot 3.x
- Spring Boot Actuator
- Either Tomcat JDBC Pool or HikariCP (for connection pool metrics)

## Dependency Management

This library uses `provided` scope for Spring Boot and Micrometer dependencies to avoid version conflicts. This means:

1. If your project is a Spring Boot application:
   - No additional configuration needed
   - Dependencies are automatically managed by Spring Boot's parent POM
   - This is the recommended setup

2. If your project is NOT a Spring Boot application:
   - You must explicitly add the required dependencies
   - See the "Required Dependencies" section below
   - Or consider using `compile` scope in your local build

## Required Dependencies

When using this metrics logger in a non-Spring Boot project, you need to ensure the following dependencies are available:

### For Spring Boot 2.x (system-metrics-boot2)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
    <version>2.7.18</version>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
    <version>1.9.12</version>
</dependency>
```

### For Spring Boot 3.x (system-metrics-boot3)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
    <version>3.2.5</version>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
    <version>1.12.3</version>
</dependency>
```

## Usage

### Spring Boot 2.x Applications

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.yourorg</groupId>
    <artifactId>system-metrics-boot2</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Spring Boot 3.x Applications

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.yourorg</groupId>
    <artifactId>system-metrics-boot3</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Configuration

Add the following properties to your `application.properties` or `application.yml`:

```yaml
# Enable system metrics logging
system.metrics.enabled=true

# Optional: Configure metrics collection interval (in milliseconds)
system.metrics.interval=60000  # default is 1 minute

# Required for connection pool metrics
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

## Building

To build the project:

```bash
mvn clean install
```

## Development

### Project Structure

```
system-metrics-logger-parent/
├── system-metrics-core/        # Core functionality
├── system-metrics-boot2/       # Spring Boot 2.x implementation
└── system-metrics-boot3/       # Spring Boot 3.x implementation
```

### Adding New Metrics

To add new metrics, modify the `SystemMetricsLogger` class in the appropriate module and add the new metric collection logic.

## Troubleshooting

### Common Issues

1. **ClassNotFoundException or NoClassDefFoundError**
   - Ensure you have the required dependencies in your project
   - Check if you're using the correct Spring Boot version
   - Verify that Spring Boot Actuator is included

2. **Version Conflicts**
   - If you encounter version conflicts, consider using `compile` scope in your local build
   - Or align your project's Spring Boot version with the metrics logger version

3. **Connection Pool Metrics Not Showing**
   - Verify that Spring Boot Actuator is enabled
   - Check if you're using either Tomcat JDBC or HikariCP
   - Look for debug messages in the logs
   - Ensure the connection pool is properly configured

## License

[Your License Here]

## Contributing

[Your Contribution Guidelines Here] 