package spring.filemanipulator.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default Fallthrough for any unrecognized request url,
 * Circumventing spring boot "not found" "white-page".
 * Must be last (lowest order) in all of spring controllers precedence.
 */
@Slf4j
@RestController
@Order(Ordered.LOWEST_PRECEDENCE) // required by SwaggerUI...
public class WildcardNotFoundEndpointController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping("/{path:(?!swagger)}**")
    public RestApiError fallbackHandlerRequestedEndpointNotFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("Fallthrough log");
        RestApiError restApiError = new RestApiError(HttpStatus.NOT_FOUND);

        String uri = request.getRequestURI();
        restApiError.setApiPath(uri);

        restApiError.setErrorMessage("Unknown endpoint: " + uri);
        return restApiError;
    }
}