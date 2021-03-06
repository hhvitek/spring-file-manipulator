package spring.filemanipulator.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <pre>{@code
 * There is a custom list property called array-of-endpoint-urls
 * in the spring configuration file application.properties
 *
 * This is a "pojo" class for this custom key-value property:
     * custom-properties:
     *   array-of-endpoint-urls:
     *     - /api/file_regex_predefined_categories
     *     - /api/file_operations
     *     - /api/settings
     *     - /api/string_operation
     *     - /api/tasks
     *     - ...
 * }</pre>
 */
@Getter
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "custom-properties")
public class CustomApplicationPropertiesConfiguration {
    private final List<String> arrayOfEndpointUrls;
}