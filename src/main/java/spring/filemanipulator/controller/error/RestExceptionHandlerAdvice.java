package spring.filemanipulator.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import spring.filemanipulator.service.job.JobAlreadyFinishedException;
import spring.filemanipulator.service.task.InvalidCreateTaskParametersException;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>{@code
 * Global exception handler...
 *
 * @RestControllerAdvice annotation tells a controller that the object returned is automatically serialized into JSON
 * and passed it to the HttpResponse object. You only need to return Java body object instead of ResponseEntity object.
 * But the status could be always OK (200) although the data corresponds to exception signal (404 – Not Found for example).
 *
 * @ResponseStatus can help to set the HTTP status code for the response.
 * }</pre>
 */
@Slf4j
@RestControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    // wrapper around Errors
    private final ErrorsHelperMethods errorsHelperMethods;

    private final MessageSource messageSource;

    @Autowired
    public RestExceptionHandlerAdvice(final MessageSource messageSource, final ErrorsHelperMethods errorsHelperMethods) {
        this.errorsHelperMethods = errorsHelperMethods;
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestApiError handleEntityNotFoundException(
            ItemNotFoundException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.NOT_FOUND);
        restApiError.setErrorMessage(ex.getMessage());
        restApiError.setApiPath(httpServletRequest.getRequestURI());

        return restApiError;
    }

    @ExceptionHandler(InvalidSearchFilterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler(InvalidCreateTaskParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestApiError handleInvalidCreateTaskParametersException(
            InvalidCreateTaskParametersException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.BAD_REQUEST);
        restApiError.setErrorMessage("Validation failed: Invalid task parameters. Cannot create new Task.");
        restApiError.setErrorMessageDetail(
                errorsHelperMethods.toStringAllErrors(ex.getErrors())
        );
        restApiError.setApiPath(httpServletRequest.getRequestURI());

        return restApiError;
    }

    /**
     * Calling implicit messageSource.getMessage(messagesPathToName, null, LocaleContextHolder.getLocale());
     *      * And messages.properties does NOT contain label specified in the variable messagesPathToName.
     */
    @ExceptionHandler(NoSuchMessageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestApiError handleNoSuchMessageException(
            NoSuchMessageException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        restApiError.setErrorMessage("I18n internal error. No value found in messages.properties file.");
        restApiError.setErrorMessageDetail(ex.getMessage());
        restApiError.setApiPath(httpServletRequest.getRequestURI());
        return restApiError;
    }

    @ExceptionHandler(JpaSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestApiError handleJpaSystemException(
            JpaSystemException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        restApiError.setErrorMessage("General JPA error. Probable DB issue.");
        restApiError.setErrorMessageDetail(ExceptionUtils.getRootCauseMessage(ex));
        restApiError.setApiPath(httpServletRequest.getRequestURI());
        return restApiError;
    }

    @ExceptionHandler(JobAlreadyFinishedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestApiError handleJobAlreadyFinishedException(
            JobAlreadyFinishedException ex,
            HttpServletRequest httpServletRequest    ) {
        RestApiError restApiError = new RestApiError(HttpStatus.BAD_REQUEST);
        restApiError.setErrorMessage("The scheduled Job cannot be stopped because it already finished.");
        restApiError.setErrorMessageDetail(ExceptionUtils.getRootCauseMessage(ex));
        restApiError.setApiPath(httpServletRequest.getRequestURI());
        return restApiError;
    }

/*    @ExceptionHandler(JDBCException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestApiError handleSqlException(
            JDBCException ex,
            HttpServletRequest httpServletRequest) {
        RestApiError restApiError = new RestApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        restApiError.setErrorMessage("Database issue encountered.");
        restApiError.setErrorMessageDetail(ExceptionUtils.getRootCauseMessage(ex));
        restApiError.setApiPath(httpServletRequest.getRequestURI());
        log.info(restApiError.toString());
        return restApiError;
    }*/

    /**
     * Corrupt json serialization.
     * Calling implicit messageSource.getMessage(messagesPathToName, null, LocaleContextHolder.getLocale());
     *      * And messages.properties does NOT contain label specified in the variable messagesPathToName.
     */
    @Override
    protected @NotNull ResponseEntity<Object> handleHttpMessageNotWritable(
            @NotNull HttpMessageNotWritableException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request) {
        if (ExceptionUtils.hasCause(ExceptionUtils.getRootCause(ex), NoSuchMessageException.class)) {
            throw (NoSuchMessageException) ExceptionUtils.getRootCause(ex);
        } else {
            return super.handleHttpMessageNotWritable(ex, headers, status, request);
        }
    }

    /**
     * If using validations such as @Valid @Validate etc. On Error this exception should be thrown.
     */
    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(
            @NotNull MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            WebRequest request) {
        RestApiError restApiError = new RestApiError(status);
        restApiError.setErrorMessage("Validation failed. Cannot proceed.");
        restApiError.setErrorMessageDetail(
                errorsHelperMethods.toStringAllErrors(ex)
        );
        restApiError.setApiPath(request.getContextPath());

        return new ResponseEntity<>(restApiError, headers, status);
    }

    @Override
    protected @NotNull ResponseEntity<Object> handleHttpMessageNotReadable(
            @NotNull HttpMessageNotReadableException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request) {
        RestApiError restApiError = new RestApiError(status);
        restApiError.setErrorMessage(
                messageSource.getMessage("errors.HttpMessageNotReadable", null, LocaleContextHolder.getLocale())
        );
        restApiError.setErrorMessageDetail(ExceptionUtils.getRootCauseMessage(ex));
        return new ResponseEntity<>(restApiError, headers, status);
    }


    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        RestApiError restApiError = new RestApiError(status);
        restApiError.setErrorMessage(ExceptionUtils.getRootCauseMessage(ex));
        restApiError.setApiPath(((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(restApiError, headers, status);
    }

    /**
     * A single place to customize the response body of all exception types.
     * The default implementation sets the WebUtils.ERROR_EXCEPTION_ATTRIBUTE request attribute
     * and creates a ResponseEntity from the given body, headers, and status.

    @Override
    protected @NotNull ResponseEntity<Object> handleExceptionInternal(
            @NotNull Exception ex,
            Object body,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request) {
        RestApiError restApiError = new RestApiError(status);
        restApiError.setErrorMessage(ExceptionUtils.getRootCauseMessage(ex));
        restApiError.setErrorMessageDetail(Arrays.stream(ExceptionUtils.getRootCauseStackTrace(ex)).collect(Collectors.joining()));
        restApiError.setApiPath(((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(restApiError, headers, status);
    }*/
}