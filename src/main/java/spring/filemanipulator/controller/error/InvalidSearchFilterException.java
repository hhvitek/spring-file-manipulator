package spring.filemanipulator.controller.error;

import lombok.Getter;

/**
 * /search?filter=field:value syntax error
 */
@Getter
public class InvalidSearchFilterException extends IllegalArgumentException {

    private final String originalCause;

    public InvalidSearchFilterException(String message, String originalCause) {
        super(message);
        this.originalCause = originalCause;
    }


}
