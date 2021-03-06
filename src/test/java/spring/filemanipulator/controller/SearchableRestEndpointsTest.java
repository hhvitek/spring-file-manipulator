package spring.filemanipulator.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;
import spring.filemanipulator.repository.FileRegexPredefinedCategoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// cannot use @WebMvcMock because internal default spring boot error handling...
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SearchableRestEndpointsTest {

    @Autowired
    private FileRegexPredefinedCategoryRepository repository;

    private static String CATEGORY_ENDPOINT_URL = "/api/file_regex_predefined_categories";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;


/*    @BeforeEach
    public void init() {
        FileRegexPredefinedCategoryEntity entity1 = new FileRegexPredefinedCategoryEntity("name", "regex");
        entity1.setId(1);
        FileRegexPredefinedCategoryEntity entity2 = new FileRegexPredefinedCategoryEntity("name2", "regex2");
        entity2.setId(2);

        Mockito.when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(entity1, entity2));
    }*/

    @Test
    public void searchEndpointExistsAndSuccessfullyFetchOneRecordFromDBTest() throws Exception {
        String url = CATEGORY_ENDPOINT_URL + "/search?filter=id:1";

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].unique_name_id").value("audio"));
    }

    @Test
    public void searchEndpointNoUrlParameterReturnAllTest() throws Exception {
        String url = CATEGORY_ENDPOINT_URL + "/search";

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].unique_name_id").value("audio"))
                .andExpect(jsonPath("$[1].unique_name_id").value("video"));
    }

    @Test
    public void searchEndpointOnlyPartUrlParameterErrorTest() throws Exception {
        String url = CATEGORY_ENDPOINT_URL + "/search?filter=";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("\"status_code\" : 400"); // why additional space around " : "?
    }

    @Test
    public void searchEndpointInvalidSyntaxFormatUrlParameterErrorTest() throws Exception {
        String url = CATEGORY_ENDPOINT_URL + "/search?filter=sjksajkldsa";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("\"status_code\" : 400"); // why additional space around " : "?
    }

    @Test
    public void searchEndpointInvalidSemanticFormatUrlParameterErrorTest() throws Exception {
        String url = CATEGORY_ENDPOINT_URL + "/search?filter=id>x";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("\"status_code\" : 400"); // why additional space around " : "?
    }
}