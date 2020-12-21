package spring.filemanipulator.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/**")
public class EndpointNotFoundController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GetMapping
    public RestApiError fallbackHandlerRequestedEndpointNotFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RestApiError restApiError = new RestApiError(HttpStatus.NOT_FOUND);

        String uri = request.getRequestURI();
        restApiError.setApiPath(uri);

        restApiError.setErrorMessage("Unknown endpoint: " + uri);
        return restApiError;
    }
}
