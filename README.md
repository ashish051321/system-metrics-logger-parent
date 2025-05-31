# System Metrics Logger

A Spring Boot library that provides system metrics logging functionality for both Spring Boot 2.x and 3.x applications.

## Features

- CPU usage monitoring
- Memory usage tracking
- Thread count monitoring
- Compatible with both Spring Boot 2.x and 3.x
- Auto-configuration support
- Configurable logging interval

## Modules

- `system-metrics-core`: Core functionality and utilities
- `system-metrics-boot2`: Spring Boot 2.x implementation
- `system-metrics-boot3`: Spring Boot 3.x implementation

## Requirements

### For Spring Boot 2.x
- Java 8 or higher
- Spring Boot 2.x
- Spring Boot Actuator

### For Spring Boot 3.x
- Java 17 or higher
- Spring Boot 3.x
- Spring Boot Actuator

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

## License

[Your License Here]

## Contributing

[Your Contribution Guidelines Here] 