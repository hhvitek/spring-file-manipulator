package spring.filemanipulator.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;
import spring.filemanipulator.repository.FileRegexPredefinedCategoryRepository;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {FileRegexPredefinedCategoryController.class})
class FileRegexPredefinedCategoryControllerTest {

    @Value("${custom-properties.array-of-endpoint-urls[0]}")
    private String endpointUrl;

    @MockBean
    private FileRegexPredefinedCategoryRepository repository;
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
                        .get(endpointUrl)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
