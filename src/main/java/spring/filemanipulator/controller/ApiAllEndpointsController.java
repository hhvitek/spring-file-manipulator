package spring.filemanipulator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiAllEndpointsController {

    @Autowired
    private RequestMappingHandlerMapping requestHandlerMapping;

    @GetMapping
    public List<String> getAll() {
        URI fileOperationsLink = linkTo(methodOn(FileOperationController.class).getAll()).toUri();
        URI stringOperationsLink = linkTo(methodOn(StringOperationController.class).getAll()).toUri();
        URI fileRegexesLink = linkTo(methodOn(FileRegexPredefinedCategoryController.class).getAll()).toUri();
        URI fileRegexesSearchLink = linkTo(methodOn(FileRegexPredefinedCategoryController.class).getManyBySearchFilter("id:1")).toUri();
        URI settingsLink = linkTo(methodOn(SettingsController.class).getAll()).toUri();
        URI tasksLink = linkTo(methodOn(TaskController.class).getAll()).toUri();
        URI taskSearchLink = linkTo(methodOn(TaskController.class).getManyBySearchFilter("id:1")).toUri();
        URI taskStatusServices = linkTo(methodOn(TaskController.class).getTaskStatuses()).toUri();

        Map<String, URI> links = new HashMap<>();
        links.put("file_operations", fileOperationsLink);
        links.put("string_operations", stringOperationsLink);
        links.put("file_regex_predefined_categories", fileRegexesLink);
        links.put("file_regex_predefined_categories/search?filter=id:1", fileRegexesSearchLink);
        links.put("settings", settingsLink);
        links.put("tasks", tasksLink);
        links.put("tasks/search?filter=id:1", taskSearchLink);
        links.put("tasks/task_statuses", taskStatusServices);

        List<String> list = new ArrayList<>();
        for (RequestMappingInfo requestMappingInfo: requestHandlerMapping.getHandlerMethods().keySet()) {
            Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
            Set<String> urls =  requestMappingInfo.getPatternsCondition().getPatterns();

            if (!methods.isEmpty() && !urls.isEmpty()) {
                String methodName = methods.iterator().next().name();
                if ("GET".equalsIgnoreCase(methodName)) {
                    String apiPathPattern = urls.iterator().next();
                    list.add(apiPathPattern);
                }
            }
        }

        list.sort(Comparator.naturalOrder());

        return list;




        //return links;
    }
}