package spring.filemanipulator.controller.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
public class ErrorsContainerImpl implements ErrorsContainer{

    private Errors errors;

    private final MessageSource messageSource;

    @Autowired
    public ErrorsContainerImpl(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean hasErrors() {
        return errors.hasErrors();
    }

    @Override
    public int getErrorsCount() {
        return errors.getErrorCount();
    }

    @Override
    public String toStringAllErrors() {
        String headerMessage = "Encountered errors count: " + getErrorsCount();

        String errorsMessage = stringifyErrors(errors.getAllErrors());

        return headerMessage + System.lineSeparator() + errorsMessage;
    }

    private String stringifyErrors(List<ObjectError> errors) {
        return errors.stream()
                .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String toStringOnlyGlobalErrors() {
        return stringifyErrors(errors.getGlobalErrors());
    }

    @Override
    public String toStringOnlyFieldErrors() {
        return stringifyErrors(errors.getFieldErrors().stream().collect(Collectors.toList()));
    }


}
