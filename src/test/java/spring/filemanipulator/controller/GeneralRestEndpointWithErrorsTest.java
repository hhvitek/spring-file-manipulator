package spring.filemanipulator.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import spring.filemanipulator.configuration.CustomApplicationPropertiesConfiguration;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;
import spring.filemanipulator.repository.FileRegexPredefinedCategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// cannot use @WebMvcTest because internal default spring boot error handling...
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GeneralRestEndpointWithErrorsTest {

    // faked http, direct method call, no network
    // also no redirection (aka /error endpoint)
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileRegexPredefinedCategoryRepository fileRegexPredefinedCategoryRepository;

    // whole context
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomApplicationPropertiesConfiguration customApplicationProperties;

    private List<String> endpointsUrls;

    @BeforeEach
    public void init() {
        FileRegexPredefinedCategoryEntity entity = new FileRegexPredefinedCategoryEntity("name1", "regex1");
        entity.setId(1);
        Mockito.when(fileRegexPredefinedCategoryRepository.findById(1)).thenReturn(Optional.of(entity));

        Mockito.when(fileRegexPredefinedCategoryRepository.findById(2)).thenReturn(Optional.empty());

        assertThat(customApplicationProperties).isNotNull();
        endpointsUrls = customApplicationProperties.getArrayOfEndpointUrls();
        assertThat(endpointsUrls).isNotNull();
    }

    @Test
    public void doEndpointsExistTest() throws Exception {
        for (String endpointUrl: endpointsUrls) {
            mockMvc.perform(get(endpointUrl).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void getOneByIdExistingReturns200() throws Exception {
        String url = "/api/tasks" + "/1";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void getOneByIdNonFoundReturns404() throws Exception {
        String url = "/api/tasks" + "/99";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(404));
    }

    // cannot use mockMvc - do not support redirection
    @Test
    public void getOneByIdInvalidUrlParameterReturns404Test() throws Exception {
        String url = "/api/tasks" + "/x2x";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("\"status_code\" : 404"); // why additional space around " : "?
    }

    // cannot use mockMvc - do not support redirection
    @Test
    public void unknownEndpointReturns404Test() {
        String unknownEndpoint = "/custom/unknown";

        ResponseEntity<String> response = restTemplate.getForEntity(unknownEndpoint, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("\"status_code\" : 404"); // why additional space around " : "?
    }

    // cannot use mockMvc - do not support redirection
    @Test
    public void unknownEndpointContainsCorrectApiPath() {
        String unknownEndpoint = "/custom/unknown";

        ResponseEntity<String> response = restTemplate.getForEntity(unknownEndpoint, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("\"status_code\" : 404"); // why additional space around " : "?
        assertThat(response.getBody()).contains("\"api_path\" : \"" + unknownEndpoint + "\""); // why additional space around " : "?
    }

    @Test
    public void methodNotAllowedTest() throws Exception {
        String existingUrl = "/api/tasks";

        mockMvc.perform(delete(existingUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(405))
                .andExpect(jsonPath("$.api_path").value(existingUrl))
                .andExpect(jsonPath("$.error_message").value("HttpRequestMethodNotSupportedException: Request method 'DELETE' not supported"))
                .andExpect(jsonPath("$.error_message_detail").doesNotExist());
    }

    @Test
    public void callErrorEndpointDirectlyTest() throws Exception {
        String url = "/error";
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(400));
    }

}