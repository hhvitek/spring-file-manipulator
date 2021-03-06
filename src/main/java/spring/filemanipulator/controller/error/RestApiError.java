package spring.filemanipulator.controller.error;

import lombok.*;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Whenever error/exception etc. is encountered during user's request processing,
 * than this class is used as customized error response.
 *
 * It's implemented in such a way that it's toString() method is in the JSON format.
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestApiError {

    private static final int HTTP_CODE_MIN_VALUE = 200;
    private static final int HTTP_CODE_MAX_VALUE = 599;

    @Setter(AccessLevel.NONE)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Min(HTTP_CODE_MIN_VALUE)
    @Max(HTTP_CODE_MAX_VALUE)
    @Setter(AccessLevel.NONE)
    private int statusCode;

    @NonNull
    @Setter(AccessLevel.NONE)
    private String statusError;

    private String errorMessage;
    private String errorMessageDetail;

    private String apiPath;

    public RestApiError(HttpStatus httpStatus) {
        statusCode = httpStatus.value();
        statusError = httpStatus.getReasonPhrase();
    }

    public Map<String, Object> toAttributeMap() {
        Map<String, Object> linkedMap = new LinkedHashMap<>();
        linkedMap.put("timestamp", timestamp);
        linkedMap.put("status_code", statusCode);
        linkedMap.put("status_error", statusError);
        linkedMap.put("error_message", errorMessage);
        linkedMap.put("error_message_detail", errorMessageDetail);
        linkedMap.put("api_path", apiPath);

        return Collections.unmodifiableMap(linkedMap);
    }

    @Override
    public String toString() {
        return toAttributeMap().toString();
    }
}