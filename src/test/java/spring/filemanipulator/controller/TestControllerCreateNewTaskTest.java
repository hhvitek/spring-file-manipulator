package spring.filemanipulator.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import spring.filemanipulator.service.task.InvalidCreateTaskParametersException;
import spring.filemanipulator.service.task.TaskService;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTO;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTOValidator;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TestControllerCreateNewTaskTest {

    private static final String ENDPOINT_CREATE_NEW_TASK = "/api/tasks/createNewTask";

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreateTaskParametersDTOValidator dtoValidator;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    public void init() throws Exception {
        Mockito.when( taskService.createAndSchedule( org.mockito.Matchers.any() ) )
                .thenAnswer(invocationOnMock -> {
                    CreateTaskParametersDTO dto = invocationOnMock.getArgument(0, CreateTaskParametersDTO.class );
                    if (!dtoValidator.isValid(dto)) {
                        throw new InvalidCreateTaskParametersException(dtoValidator.getLastErrors());
                    }
                    return null;
                });


        mockMvc.perform(get("/api/tasks/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getHttpMethodNotSupportedTest() throws Exception {
        mockMvc.perform(get(ENDPOINT_CREATE_NEW_TASK).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void emptyBodyFailTest() throws Exception {
        mockMvc.perform(
                post(ENDPOINT_CREATE_NEW_TASK)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message", containsString("Http message is malformed. Please check request's format.")));

    }

    @Test
    public void curlyBracketsJsonOnlyBodyFailTest() throws Exception {
        String body = "{}";

        mockMvc.perform(
                post(ENDPOINT_CREATE_NEW_TASK)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message", containsString("Validation failed. Cannot proceed.")));

    }

    @Test
    public void malformedJsonBody() throws Exception {

        String body = "\\";

        mockMvc.perform(
            post(ENDPOINT_CREATE_NEW_TASK)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message", containsString("Http message is malformed. Please check request's format.")))
                .andExpect(jsonPath("$.error_message_detail", containsString("JsonParseException: Unexpected character")));
    }

    @Test
    public void missingJsonAttribute() throws Exception {
        String body = "{\n"
                + "\"destination_folder\": \"./destination-folder\",\n"
                + "\"syntax_and_pattern\": \"glob:pattern\",\n"
                + "\"file_operation\": \"COPY\"\n"
                + "}\n";

        mockMvc.perform(
                post(ENDPOINT_CREATE_NEW_TASK)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message", containsString("Validation failed. Cannot proceed.")))
                .andExpect(jsonPath("$.error_message_detail", containsString("Encountered errors count: 2")));
    }

    @Test
    public void camelCasedAttributes() throws Exception {

        String sourceFolder = "./src/test/resources/controller/source-folder/";

        Path path = Path.of(sourceFolder);
        Assertions.assertTrue(Files.isDirectory(path), "Source folder not found in: " + sourceFolder);

        String body = "{\n"
                + "\"source_folder\": \"" + sourceFolder + "\",\n"
                + "\"destinationFolder\": \"./controller/destination-folder\",\n"
                + "\"syntax_and_pattern\": \"glob:pattern\",\n"
                + "\"file_operation\": \"COPY\",\n"
                + "\"string_operation\": \"NICE_LOOKING_FILENAME\""
                + "}\n";

        mockMvc.perform(
                post(ENDPOINT_CREATE_NEW_TASK)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message", containsString("Validation failed. Cannot proceed.")))
                .andExpect(jsonPath("$.error_message_detail", containsString("Encountered errors count: 1")));
    }

    @Test
    public void jsonFormatOkButSourceFolderDoesNotExist() throws Exception {
        String sourceFolder = "./src/test/resources/controller/source-folder-DOESNOTEXISTS/";

        Path path = Path.of(sourceFolder);
        Assertions.assertFalse(Files.isDirectory(path), "Source folder found, but it should not exist!!" + sourceFolder);

        String body = "{\n"
                + "\"source_folder\": \"" + sourceFolder + "\",\n"
                + "\"destinationFolder\": \"./controller/destination-folder\",\n"
                + "\"syntax_and_pattern\": \"glob:pattern\",\n"
                + "\"file_operation\": \"COPY\",\n"
                + "\"string_operation\": \"NICE_LOOKING_FILENAME\""
                + "}\n";

        mockMvc.perform(
                post(ENDPOINT_CREATE_NEW_TASK)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message", containsString("Validation failed. Cannot proceed.")))
                .andExpect(jsonPath("$.error_message_detail", containsString("Encountered errors count: 1")));
    }

    @Test
    public void bodyOkTest() throws Exception {
        String sourceFolder = "./src/test/resources/controller/source-folder/";

        Path path = Path.of(sourceFolder);
        Assertions.assertTrue(Files.isDirectory(path), "Source folder not found in: " + sourceFolder);

        String body = "{\n"
                + "\"source_folder\": \"" + sourceFolder + "\",\n"
                + "\"destination_folder\": \"./controller/destination-folder\",\n"
                + "\"syntax_and_pattern\": \"glob:pattern\",\n"
                + "\"file_operation\": \"COPY\",\n"
                + "\"string_operation\": \"NICE_LOOKING_FILENAME\""
                + "}\n";

        mockMvc.perform(
                post(ENDPOINT_CREATE_NEW_TASK)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isCreated());
    }

}