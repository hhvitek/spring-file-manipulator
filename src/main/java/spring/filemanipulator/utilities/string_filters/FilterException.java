package spring.filemanipulator.utilities.string_filters;

public class FilterException extends RuntimeException {
    private static final long serialVersionUID = -2738144329502932175L;

    protected ErrorCode errorCode = ErrorCode.OK;
    protected String errorParameter = "Ok.";

    public FilterException() {
    }

    public FilterException(String message) {
        super(message);
    }

    public FilterException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public FilterException(ErrorCode errorCode, String errorParameter) {
        this(errorCode);
        this.errorParameter = errorParameter;
    }

    public String errorMessage() {
        switch (errorCode) {
            case OK:
                return "Ok.";
            case ILLEGAL_ARGUMENT_NOT_LEGAL_SIZE:
                return String.format("Argument -%s is not allowed. Should be one character long", errorParameter);
            case ILLEGAL_FILTER_PATTERN_SYNTAX:
                return String.format("Filter pattern invalid: %s.", errorParameter);
            case UNSUPPORTED_OPERATION:
                return String.format("This operation is not supported: %s.", errorParameter);
        }
        return "";
    }
}