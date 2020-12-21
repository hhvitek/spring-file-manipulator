package spring.filemanipulator.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * application properties "pojo" for custom key-value properties.
 */
@Getter
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "custom-properties")
public class CustomApplicationPropertiesConfiguration {

    private List<String> arrayOfEndpointUrls;
}
