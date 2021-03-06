package spring.filemanipulator.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import spring.filemanipulator.controller.error.ErrorsHelperMethods;
import spring.filemanipulator.controller.error.RestExceptionHandlerAdvice;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;
import spring.filemanipulator.repository.FileRegexPredefinedCategoryRepository;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {FileRegexPredefinedCategoryController.class, RestExceptionHandlerAdvice.class})
class FileRegexPredefinedCategoryControllerTest {

    private static String CATEGORY_ENDPOINT_URL = "/api/file_regex_predefined_categories";

    @MockBean
    private FileRegexPredefinedCategoryRepository repository; // mock behaviour must be defined

    @MockBean
    private ErrorsHelperMethods errorsHelperMethods; // required by RestExceptionHandlerAdvice, no specific behaviour required, just mock

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllReturnsListTest() throws Exception {

        FileRegexPredefinedCategoryEntity entity1 = new FileRegexPredefinedCategoryEntity("name", "regex");
        entity1.setId(1);
        FileRegexPredefinedCategoryEntity entity2 = new FileRegexPredefinedCategoryEntity("name2", "regex2");
        entity2.setId(2);

        Mockito.when(repository.findAll())
                .thenReturn(Arrays.asList(entity1, entity2));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(CATEGORY_ENDPOINT_URL)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1));
    }
}