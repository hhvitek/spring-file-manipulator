package spring.filemanipulator.service.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import spring.filemanipulator.service.operations.FileOperationService;
import spring.filemanipulator.service.operations.FileOperationServiceImpl;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = { CreateTaskParametersDTOValidator.class, FileOperationServiceImpl.class})
class CreateTaskParametersDTOValidatorTest {

    private final CreateTaskParametersDTOValidator validator;

    private final ResourceLoader resourceLoader;

    @MockBean
    private FileOperationService fileOperationService;

    @Autowired
    CreateTaskParametersDTOValidatorTest(final CreateTaskParametersDTOValidator validator, ResourceLoader resourceLoader) {
        this.validator = validator;
        this.resourceLoader = resourceLoader;
    }

    @BeforeEach
    public void init() {
        Mockito.when(fileOperationService.existsByUniqueNameId("COPY"))
                .thenReturn(true);

        Mockito.when(fileOperationService.existsByUniqueNameId("UNKNOWN"))
                .thenReturn(false);
    }


    @Test
    public void emptyDTOTest() {
        CreateTaskParametersDTO dto = new CreateTaskParametersDTO();
        Errors errors = new BeanPropertyBindingResult(dto, "dto");

        ValidationUtils.invokeValidator(validator, dto, errors);

        assertThat(errors.getErrorCount()).isEqualTo(4);
    }

    @Test
    public void twoValidtwoEmptyDTOTest() {
        CreateTaskParametersDTO dto = new CreateTaskParametersDTO(
                "U:\\sourceNotExist",
                "U:\\destNotExistButIsOkPathVariable",
                "glob:\\d+",
                "COPY");
        Errors errors = new BeanPropertyBindingResult(dto, "dto");

        ValidationUtils.invokeValidator(validator, dto, errors);

        assertThat(errors.getErrorCount()).isEqualTo(1);
    }

    @Test
    public void isValidDTOTest() throws IOException {
        assertThat(resourceLoader).isNotNull();

        CreateTaskParametersDTO dto = getDTOWithValidParameters();
        Errors errors = new BeanPropertyBindingResult(dto, "dto");

        ValidationUtils.invokeValidator(validator, dto, errors);
        assertThat(errors.getErrorCount()).isEqualTo(0);
    }

    private CreateTaskParametersDTO getDTOWithValidParameters() throws IOException {
        Resource existingFolderResource = resourceLoader.getResource("classpath:service/task");
        assertThat(existingFolderResource.exists());
        assertThatNoException().isThrownBy(
                () -> existingFolderResource.getFile()
        );
        String existingFolder = existingFolderResource.getFile().getAbsolutePath().toString();

        return new CreateTaskParametersDTO(
                existingFolder,
                "U:\\destNotExistButIsOkPathVariable",
                "glob:\\d+",
                "COPY");
    }


}
