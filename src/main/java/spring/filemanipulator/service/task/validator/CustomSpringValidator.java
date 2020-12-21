package spring.filemanipulator.service.task.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public interface CustomSpringValidator<T> extends Validator {
    boolean isValid(T validatedObject);
    boolean hasErrors();
    Errors getLastErrors();

}
