package spring.filemanipulator.controller.error;

import lombok.*;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Whenever error/exception etc. is encountered during user's request processing,
 * This class represents "pojo" json format response.
 */
@Data
@RequiredArgsConstructor
@Log
public class RestApiError {

    @Setter(AccessLevel.NONE)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Min(0) @Max(599)
    private int statusCode;
    @NonNull
    private String statusError;

    private String errorMessage;
    private String errorMessageDetail;

    private String apiPath;

    public RestApiError(HttpStatus httpStatus) {
        statusCode = httpStatus.value();
        statusError = httpStatus.getReasonPhrase();
    }

    public static RestApiError fromDefaultAttributeMap(
            final Map<String, Object> defaultAttributeMap
    ) {
        if (isAttributeMapValid(defaultAttributeMap)) {
            Integer statusCode = (Integer) defaultAttributeMap.get("status");
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

            RestApiError restApiError = new RestApiError(httpStatus);
            restApiError.setErrorMessage((String) defaultAttributeMap.getOrDefault("message", "No message available."));
            restApiError.setErrorMessageDetail((String) defaultAttributeMap.getOrDefault("trace", "No error available."));
            restApiError.setApiPath((String) defaultAttributeMap.getOrDefault("path", "No path available."));
            return restApiError;
        } else {
            RestApiError restApiError = new RestApiError(HttpStatus.INTERNAL_SERVER_ERROR);
            restApiError.setErrorMessage("Unexpected DefaultAttributeMap format!");
            restApiError.setErrorMessageDetail(defaultAttributeMap.toString());
            restApiError.setApiPath((String) defaultAttributeMap.getOrDefault("path", "No path available."));
            return restApiError;
        }
    }

    private static boolean isAttributeMapValid(Map<String, Object> attributeMap) {
        try {
            Object statusCodeValue = attributeMap.get("status");
            Integer statusCode = (Integer) (statusCodeValue); // ClassCastException, NullPointerException
            if (statusCode >= 200 && statusCode <= 599) {
                return true;
            } else {
                return false;
            }
        } catch (ClassCastException | NullPointerException | IllegalArgumentException ex) {
            return false;
        }
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
}
