package spring.filemanipulator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class FileOperationControllerTest {

    // faked http, direct method call, no network
    // also no redirection (aka /error endpoint)
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void i18nLocalizationTest() throws Exception {
        String url = "/api/file_operations?lang=cs_CZ";

        String expected = "KOPÍROVÁNÍ";

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].unique_name_id").value("COPY"))
                .andExpect(jsonPath("$[0].name").value(expected));
    }

    @Test
    public void noLocalizationTest() throws Exception {
        String url = "/api/file_operations";

        String expected = "COPY";

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].unique_name_id").value("COPY"))
                .andExpect(jsonPath("$[0].name").value(expected));
    }

}