package spring.filemanipulator.service.task;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;

@Getter
public class InvalidCreateTaskParametersException extends RuntimeException {

    private Errors errors;

    public InvalidCreateTaskParametersException(@NotNull Errors errors) {
        super("Invalid task parameters. Errors count: " + errors.getErrorCount());
        this.errors = errors;
    }
}
