package spring.filemanipulator.controller.error;

import org.springframework.validation.Errors;

/**
 * Helper-wrapper around Spring's Errors ()
 */
public interface ErrorsContainer {
    Errors getErrors();
    void setErrors(Errors errors);

    boolean hasErrors();
    int getErrorsCount();

    String toStringAllErrors();
    String toStringOnlyGlobalErrors();
    String toStringOnlyFieldErrors();
}
