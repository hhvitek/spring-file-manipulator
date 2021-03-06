package spring.filemanipulator.service.task.validator;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import spring.filemanipulator.service.operations.FileOperationService;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Component("beforeCreateCreateTaskParametersDTOValidator")
public class CreateTaskParametersDTOValidator implements CustomSpringValidator<CreateTaskParametersDTO> {

    private static final String MESSAGES_PATH_PREFIX = "task.create.errors.";

    private final FileOperationService fileOperationService;

    private Errors lastErrors;

    @Autowired
    public CreateTaskParametersDTOValidator(final FileOperationService fileOperationService) {
        this.fileOperationService = fileOperationService;
    }

    // which bean to validate
    @Override
    public boolean supports(@NotNull Class<?> aClass) {
        return CreateTaskParametersDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NotNull Object o, @NotNull Errors errors) {
        CreateTaskParametersDTO dto = (CreateTaskParametersDTO) o;

        if (!isValidPathAndDoesFolderExist(dto.getSourceFolder())) { // must exist
            errors.rejectValue(
                    "sourceFolder",
                    MESSAGES_PATH_PREFIX + "sourceFolder.notExists",
                    new Object[]{(dto.getSourceFolder())},
                    "Source folder does not exist."
            );
        }

        if (!isValidPathVariable(dto.getDestinationFolder())) { // doesn't have to exist
            errors.rejectValue(
                    "destinationFolder",
                    MESSAGES_PATH_PREFIX + "destinationFolder",
                    new Object[]{(dto.getDestinationFolder())},
                    "Destination folder is not valid path variable."
            );
        }

        if (!isValidPathMatcherSyntaxAndPattern(dto.getSyntaxAndPattern())) {
            errors.rejectValue(
                    "syntaxAndPattern",
                    MESSAGES_PATH_PREFIX + "syntaxAndPattern",
                    "SyntaxAndPattern must be in syntax:pattern format."
            );
        }

        if (!doesFileOperationExistByUniqueNameId(dto.getFileOperation())){
            String fileOperations = fileOperationService.getAllUniqueNameIds().toString();
            errors.rejectValue(
                    "fileOperation",
                    MESSAGES_PATH_PREFIX + "fileOperation",
                    new Object[]{(fileOperations)},
                    "File operation is not recognized."
            );
        }
    }

    private boolean isValidPathVariable(String file) {
        try {
            Path.of(file);
            return true;
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }

    private boolean isValidPathAndDoesFolderExist(String folder) {
        try {
            Path path = Path.of(folder);
            return Files.isDirectory(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }

    private boolean isValidPathMatcherSyntaxAndPattern(String syntaxAndPattern) {
        try {
            FileSystems.getDefault().getPathMatcher(syntaxAndPattern);
            return true;
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException ex) {
            // IllegalArgumentException – If the parameter does not take the form: syntax:pattern
            // java.util.regex.PatternSyntaxException – If the pattern is invalid
            // UnsupportedOperationException – If the pattern syntax is not known to the implementation
            // NullPointerException is forbidden here...
            return false;
        }
    }

    private boolean doesFileOperationExistByUniqueNameId(String uniqueNameId) {
        return fileOperationService.existsByUniqueNameId(uniqueNameId);
    }


    @Override
    public boolean isValid(CreateTaskParametersDTO validatedObject) {
        lastErrors = new BeanPropertyBindingResult(validatedObject, "dto");
        validate(validatedObject, lastErrors);

        return !lastErrors.hasErrors();
    }

    @Override
    public boolean hasErrors() {
        return lastErrors != null && lastErrors.hasErrors();
    }

    @Override
    public Errors getLastErrors() {
        return lastErrors;
    }
}