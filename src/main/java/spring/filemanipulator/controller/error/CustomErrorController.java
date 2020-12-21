package spring.filemanipulator.controller.error;

//@RestController
//@RequestMapping(path = "${server.error.path:/error}", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomErrorController /*extends AbstractErrorController*/ {
/*

    @Autowired
    public CustomErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        RestApiError restApiError = new RestApiError(httpStatus);

        String uri  = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();
        restApiError.setApiPath(uri);

        restApiError = updateMessageIfMessageExists(restApiError, request);
        restApiError = updateMessageAndDetailIfExceptionExists(restApiError, request);

        if (httpStatus == HttpStatus.NOT_FOUND) {
            restApiError = updateMessageBecauseNotFound(restApiError, uri);
        }

        return new ResponseEntity(restApiError, httpStatus);
    }

    private RestApiError updateMessageIfMessageExists(RestApiError restApiError, HttpServletRequest request) {
        Object exceptionNullable = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String message = exceptionNullable.toString();
        restApiError.setErrorMessage(message);
        return restApiError;
    }

    private RestApiError updateMessageAndDetailIfExceptionExists(RestApiError restApiError, HttpServletRequest request) {
        Object exceptionNullable = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        getRootCauseMessageFromExceptionIfExists(exceptionNullable)
                .ifPresent(message -> restApiError.setErrorMessage(message));

        getRootCauseFromExceptionIfExists(exceptionNullable)
                .ifPresent(cause -> restApiError.setErrorMessageDetail(cause));
        return restApiError;
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

    private RestApiError updateMessageBecauseNotFound(RestApiError restApiError, String uri) {
        restApiError.setErrorMessage("Most likely the requested api endpoint: " + uri + " is unknown...");
        return restApiError;
    }


    // based on official documentation this is not used, ignored
    @Override
    public String getErrorPath() {
        return null;
    }*/
}
