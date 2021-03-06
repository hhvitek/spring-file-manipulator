package spring.filemanipulator.controller.error;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * Overrides Spring Boot default BasicErrorController (/error endpoint).
 * (Overrides Spring WhiteLabel page...)
 *
 * Replacing error page with {@link RestApiError} object
 *
 * This default error handling "fallthrough" is executed, only if there is no @ExceptionHandler
 * method for the relevant error/exception...
 */
@RestController
@RequestMapping(path = "${server.error.path:/error}", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomErrorController extends AbstractErrorController {

    @Value("${server.error.path:/error}")
    private String SERVER_ERROR_PATH;

    @Autowired
    public CustomErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        RestApiError restApiError =  new RestApiError(httpStatus);

        String originalRequestUri = getOriginalRequestUriFromRequest(request);
        restApiError.setApiPath(originalRequestUri);

        restApiError.setErrorMessage(getErrorMessageFromRequest(request));

        restApiError.setErrorMessageDetail(getErrorException(request));

        if (httpStatus == HttpStatus.NOT_FOUND) {
            restApiError.setErrorMessage("The url <" + originalRequestUri + "> is not supported.");
        }

        if (seemsLikeErrorEndpointCalledDirectly(restApiError)) {
            return new ResponseEntity(generateRestApiErrorErrorEndpointCalledDirectly(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(restApiError, httpStatus);
    }

    private String getOriginalRequestUriFromRequest(HttpServletRequest request) {
        String originalUri = String.valueOf(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));

        if (originalUri.equals("null")) {
            originalUri = request.getRequestURI();
        }

        return originalUri;
    }

    private String getErrorMessageFromRequest(HttpServletRequest request) {
        Object exceptionNullable = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if (exceptionNullable == null) {
            return null;
        } else {
            return exceptionNullable.toString();
        }
    }

    private String getErrorException(HttpServletRequest request) {
        Object exceptionNullable = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        return getRootCauseMessageFromExceptionIfExists(exceptionNullable)
                .orElse(null);
    }

    private Optional<String> getRootCauseMessageFromExceptionIfExists(Object exceptionNullable) {
        if (exceptionNullable != null) {
            Exception exception =  (Exception) exceptionNullable;
            return Optional.of(ExceptionUtils.getRootCauseMessage(exception));
        } else
            return Optional.empty();
    }

    private Optional<String> getRootCauseFromExceptionIfExists(Object exceptionNullable) {
        if (exceptionNullable != null) {
            Exception exception =  (Exception) exceptionNullable;
            return Optional.of(ExceptionUtils.getStackTrace(ExceptionUtils.getRootCause(exception)));
        } else
            return Optional.empty();
    }

    private boolean seemsLikeErrorEndpointCalledDirectly(RestApiError restApiError) {
        return restApiError.getApiPath().equals(SERVER_ERROR_PATH)
            && HttpStatus.valueOf(restApiError.getStatusCode()).equals(HttpStatus.INTERNAL_SERVER_ERROR)
            && restApiError.getErrorMessage() == null
            && restApiError.getErrorMessageDetail() == null;
    }

    private RestApiError generateRestApiErrorErrorEndpointCalledDirectly() {
        RestApiError restApiError = new RestApiError(HttpStatus.BAD_REQUEST);
        restApiError.setApiPath(SERVER_ERROR_PATH);
        restApiError.setErrorMessage("Endpoint /error is not meant to be used directly...");
        return restApiError;
    }

    // based on the official documentation this is not used, ignored
    // since 2.3.0 in favor of setting the property server.error.path
    @Override
    public String getErrorPath() {
        throw new UnsupportedOperationException("This should have not been called...like... ever...");
    }
}