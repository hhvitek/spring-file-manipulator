package spring.filemanipulator.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import spring.filemanipulator.controller.error.ErrorsHelperMethods;
import spring.filemanipulator.controller.error.RestExceptionHandlerAdvice;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.JobNotFoundException;
import spring.filemanipulator.service.job.JobService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {JobController.class, RestExceptionHandlerAdvice.class})
class JobControllerTest {

    // required by JobController
    @MockBean
    private JobService jobService;

    // required by JobController
    @MockBean
    private JobRepository jobRepository;

    // required by RestExceptionHandlerAdvice, no specific behaviour required, just mock
    @MockBean
    private ErrorsHelperMethods errorsHelperMethods;

    // faked http, direct method call, no network
    // also no redirection (aka /error endpoint)
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getMethodToStopNotSupportedTest() throws Exception {
        Mockito.doThrow(new JobNotFoundException(999))
                .when(jobService).signalToStopIfNotFoundThrow(999);

        String url = "/api/jobs/999/signalToStop";

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void signalingToStopToNonExistentJobResultsIn404Test() throws Exception {
        Mockito.doThrow(new JobNotFoundException(999))
                .when(jobService).signalToStopIfNotFoundThrow(999);

        String url = "/api/jobs/999/signalToStop";

        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(404))
                .andExpect(jsonPath("$.error_message").value("The Job (id=999) not found."))
                .andExpect(jsonPath("$.api_path").value("/api/jobs/999/signalToStop"));
    }

}