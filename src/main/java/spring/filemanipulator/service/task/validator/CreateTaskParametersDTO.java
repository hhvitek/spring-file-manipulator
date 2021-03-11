package spring.filemanipulator.service.task.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskParametersDTO {

    private static final String PATTERN_PATH_WIN_LIN = "((^([a-zA-Z]:)?(\\\\?[a-zA-Z0-9_.-]+)+\\\\?$)|(^(/?[a-zA-Z0-9_.-]+)+/?$))";

    private static final String PATTERN_SYNTAX_AND_PATTERN = "^(glob|regex):(.+)$";

    // is it Path variable and does a folder exist?
    @NotBlank(message = "{task.create.errors.sourceFolder.missing}")
    @Pattern(regexp = PATTERN_PATH_WIN_LIN, message = "{task.create.errors.sourceFolder}" )
    private String sourceFolder;

    // is it Path variable?
    @NotBlank(message = "{task.create.errors.destinationFolder.missing}" )
    @Pattern(regexp = PATTERN_PATH_WIN_LIN, message = "{task.create.errors.destinationFolder}"  )
    private String destinationFolder;

    // test if it compiles successfully
    @NotBlank(message = "{task.create.errors.syntaxAndPattern.missing}" )
    @Pattern(regexp = PATTERN_SYNTAX_AND_PATTERN, message = "{task.create.errors.syntaxAndPattern}" )
    private String syntaxAndPattern;

    // test if uniqueNameId exists
    @NotBlank(message = "{task.create.errors.fileOperation.missing}")
    private String fileOperation;

    @NotBlank(message = "{task.create.errors.stringOperation.missing}")
    private String stringOperation;

    private String stringOperationWhat;

    private String stringOperationWith;
}