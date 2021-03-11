package spring.filemanipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.filemanipulator.service.entity.NamedServiceEntity;
import spring.filemanipulator.service.operation.file.FileOperationService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/file_operations")
public class FileOperationController {

    private final FileOperationService fileOperationService;

    @Autowired
    public FileOperationController(final FileOperationService fileOperationService) {
        this.fileOperationService = fileOperationService;
    }

    @GetMapping
    public Collection<NamedServiceEntity> getAll() {
        Collection<NamedServiceEntity> serviceEntities = fileOperationService.getAllNamedEntities();
        return serviceEntities;
    }
}