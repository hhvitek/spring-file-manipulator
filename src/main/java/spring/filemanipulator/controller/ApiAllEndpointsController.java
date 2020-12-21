package spring.filemanipulator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class ApiAllEndpointsController {

    @GetMapping
    public Map<String, URI> getAll() {
        URI fileOperationsLink = linkTo(methodOn(FileOperationController.class).getAll()).toUri();
        URI stringOperationsLink = linkTo(methodOn(StringOperationController.class).getAll()).toUri();
        URI fileRegexesLink = linkTo(methodOn(FileRegexPredefinedCategoryController.class).getAll()).toUri();
        URI fileRegexesSearchLink = linkTo(methodOn(FileRegexPredefinedCategoryController.class).getManyBySearchFilter("id:1")).toUri();
        URI settingsLink = linkTo(methodOn(SettingsController.class).getAll()).toUri();
        URI tasksLink = linkTo(methodOn(TaskController.class).getAll()).toUri();
        URI taskSearchLink = linkTo(methodOn(TaskController.class).getManyBySearchFilter("id:1")).toUri();

        Map<String, URI> links = new HashMap<>();
        links.put("file_operations", fileOperationsLink);
        links.put("string_operations", stringOperationsLink);
        links.put("file_regex_predefined_categories", fileRegexesLink);
        links.put("file_regex_predefined_categories/search?filter=id:1", fileRegexesSearchLink);
        links.put("settings", settingsLink);
        links.put("tasks", tasksLink);
        links.put("tasks/search?filter=id:1", taskSearchLink);

        return links;
    }
}
