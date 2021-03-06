package spring.filemanipulator.controller.error;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
public class ErrorsHelperMethods {
    private final MessageSource messageSource;

    @Autowired
    public ErrorsHelperMethods(@NotNull final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String toStringAllErrors(@NotNull Errors errors) {
        String headerMessage = "Encountered errors count: " + errors.getErrorCount();

        String errorsMessage = stringifyErrors(errors.getAllErrors());

        return headerMessage + System.lineSeparator() + errorsMessage;
    }

    private String stringifyErrors(@NotNull List<ObjectError> errors) {
        return errors.stream()
                .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public String toStringOnlyGlobalErrors(@NotNull Errors errors) {
        return stringifyErrors(errors.getGlobalErrors());
    }

    public String toStringOnlyFieldErrors(@NotNull Errors errors) {
        return stringifyErrors(new ArrayList<>(errors.getFieldErrors()));
    }
}