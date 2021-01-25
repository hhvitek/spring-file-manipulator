package spring.filemanipulator.controller.error;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import spring.filemanipulator.service.task.InvalidCreateTaskParametersException;

import javax.servlet.http.HttpServletRequest;

/**
 * Global exception handler...
 *
 * @RestControllerAdvice annotation tells a controller that the object returned is automatically serialized into JSON
 * and passed it to the HttpResponse object. You only need to return Java body object instead of ResponseEntity object.
 * But the status could be always OK (200) although the data corresponds to exception signal (404 – Not Found for example).
 * @ResponseStatus can help to set the HTTP status code for the response.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    // wrapper around Errors
    private final ErrorsContainer errorsContainer;

    @Autowired
    public RestExceptionHandlerAdvice(final ErrorsContainer errorsContainer) {
        this.errorsContainer = errorsContainer;
    }

    @ExceptionHandler(value = { ItemNotFoundException.class } )
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public RestApiError handleEntityNotFoundException(
            ItemNotFoundException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.NOT_FOUND);
        restApiError.setErrorMessage(ex.getMessage());
        restApiError.setApiPath(httpServletRequest.getRequestURI());

        return restApiError;
    }

    @ExceptionHandler(value = { InvalidSearchFilterException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestApiError handleIllegalSearchFilterException(
            InvalidSearchFilterException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.BAD_REQUEST);
        restApiError.setErrorMessage(ex.getMessage());
        restApiError.setErrorMessageDetail(ex.getOriginalCause());
        restApiError.setApiPath(httpServletRequest.getRequestURI());

        return restApiError;
    }

    /**
     * Thrown during new Task creation, if parameters are invalid.
     */
    @ExceptionHandler(value = InvalidCreateTaskParametersException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestApiError handleInvalidCreateTaskParametersException(
            InvalidCreateTaskParametersException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.BAD_REQUEST);
        restApiError.setErrorMessage("Validation failed: Invalid task parameters. Cannot create new Task.");
        restApiError.setErrorMessageDetail(collectErrorsFromError(ex.getErrors()));
        restApiError.setApiPath(httpServletRequest.getRequestURI());

        return restApiError;
    }

    private String collectErrorsFromError(final Errors errors) {
        errorsContainer.setErrors(errors);
        return errorsContainer.toStringAllErrors();
    }

    /**
     * Calling implicit messageSource.getMessage(messagesPathToName, null, LocaleContextHolder.getLocale());
     *      * And messages.properties does NOT contain label specified in the variable messagesPathToName.
     */
    @ExceptionHandler(value = { NoSuchMessageException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestApiError handleNoSuchMessageException(
            NoSuchMessageException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        restApiError.setErrorMessage("I18n error. No value found in messages.properties file.");
        restApiError.setErrorMessageDetail(ex.getMessage());
        restApiError.setApiPath(httpServletRequest.getRequestURI());
        return restApiError;
    }

    /**
     * Corrupt json serialization.
     * Calling implicit messageSource.getMessage(messagesPathToName, null, LocaleContextHolder.getLocale());
     *      * And messages.properties does NOT contain label specified in the variable messagesPathToName.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        if (ExceptionUtils.hasCause(ExceptionUtils.getRootCause(ex), NoSuchMessageException.class)) {
            NoSuchMessageException noSuchMessageException =  (NoSuchMessageException) ExceptionUtils.getRootCause(ex);
            throw noSuchMessageException;
        } else {
            return super.handleHttpMessageNotWritable(ex, headers, status, request);
        }
    }

    /**
     * If using validations such as @Valid @Validate etc. On Error this exception should be thrown.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        RestApiError restApiError = new RestApiError(status);
        restApiError.setErrorMessage("Validation failed. Cannot proceed.");
        restApiError.setErrorMessageDetail(collectErrorsFromError(ex));
        restApiError.setApiPath(request.getContextPath());

        return new ResponseEntity<>(restApiError, headers, status);
    }

    /**
     * A single place to customize the response body of all exception types.
     * The default implementation sets the WebUtils.ERROR_EXCEPTION_ATTRIBUTE request attribute
     * and creates a ResponseEntity from the given body, headers, and status.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        RestApiError restApiError = new RestApiError(status);
        restApiError.setErrorMessage(ExceptionUtils.getRootCauseMessage(ex));
        restApiError.setErrorMessageDetail(ExceptionUtils.getStackTrace(ex));
        restApiError.setApiPath(((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(restApiError, headers, status);
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestApiError handleException(
            Exception ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        restApiError.setErrorMessage("Unknown General Error Exception.");
        restApiError.setErrorMessageDetail(ex.getMessage());
        restApiError.setApiPath(httpServletRequest.getRequestURI());

        return restApiError;
    }



}