package spring.filemanipulator.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * In the default configuration, the Swagger UI ignores any Spring's Jackson camelCases/Snake configuration.
 * This Bean (may) ensures that it's followed.
 *
 * This is just a workaround for this issue:
 * See https://github.com/springdoc/springdoc-openapi/issues/66
 */
@Configuration
public class SwaggerUIEnableJacksonCasePropagation {

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }
}