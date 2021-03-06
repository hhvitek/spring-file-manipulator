package spring.filemanipulator.controller.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * <pre>{@code
 * Override default Spring boot's error JSON response.
 * Replacing this:
 *   {
 *     "timestamp": "2021-01-25T16:56:45.760+00:00",
 *     "status": 404,
 *     "error": "Not Found",
 *     "message": "No message available",
 *     "path": "/api/file_regex_predefined_categories/2x"
 *   }
 *
 * with RestApiError
 * }</pre>
 * @see RestApiError
 */
/*
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        final Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, options);
        RestApiError restApiError = RestApiError.fromDefaultAttributeMap(defaultErrorAttributes);
        return restApiError.toAttributeMap();
    }
}
*/