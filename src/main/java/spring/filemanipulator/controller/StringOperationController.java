package spring.filemanipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.filemanipulator.service.entity.NamedServiceEntity;
import spring.filemanipulator.service.operation.string.StringOperationService;

import java.util.Collection;

@RestController
@RequestMapping("/api/string_operations")
public class StringOperationController {

    private final StringOperationService stringOperationService;

    @Autowired
    public StringOperationController(StringOperationService stringOperationService) {
        this.stringOperationService = stringOperationService;
    }

    @GetMapping
    public Collection<NamedServiceEntity> getAll() {
        return stringOperationService.getAllNamedEntities();
    }
}