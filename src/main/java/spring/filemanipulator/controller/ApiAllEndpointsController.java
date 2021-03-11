package spring.filemanipulator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiAllEndpointsController {

    @Autowired
    private RequestMappingHandlerMapping requestHandlerMapping;

    @GetMapping
    public List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (RequestMappingInfo requestMappingInfo: requestHandlerMapping.getHandlerMethods().keySet()) {
            Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
            Set<String> urls =  requestMappingInfo.getPatternsCondition().getPatterns();

            if (!methods.isEmpty() && !urls.isEmpty()) {
                String methodName = methods.iterator().next().name();
                String apiPathPattern = urls.iterator().next();
                list.add(apiPathPattern + " : " + methodName);
            }
        }

        list.sort(Comparator.naturalOrder());

        return list;
    }
}