package spring.filemanipulator.service.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskParametersDTO {

    // is it Path variable and does a folder exist?
    @NotBlank(message = "{task.create.errors.sourceFolder}")
    @Pattern(regexp = "^([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?$", message = "{task.create.errors.sourceFolder}" )
    private String sourceFolder;

    // is it Path variable?
    @NotBlank(message = "{task.create.errors.destinationFolder}" )
    @Pattern(regexp = "^([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?$", message = "{task.create.errors.destinationFolder}"  )
    private String destinationFolder;

    // test if it compiles successfully
    @NotBlank(message = "{task.create.errors.syntaxAndPattern}" )
    @Pattern(regexp = "^(glob|regex):(.+)$", message = "{task.create.errors.syntaxAndPattern}" )
    private String filePathMatcherSyntaxAndPattern;

    // test if uniqueNameId exists
    @NotBlank(message = "{task.create.errors.fileOperation}")
    private String fileOperationUniqueNameId;
}
