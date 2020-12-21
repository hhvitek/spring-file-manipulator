package spring.filemanipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.filemanipulator.service.entity.operation.file.FileOperationServiceEntity;
import spring.filemanipulator.service.operations.FileOperationService;

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
    public Collection<FileOperationServiceEntity> getAll() {
        Collection<FileOperationServiceEntity> serviceEntities = fileOperationService.getAll();
        return serviceEntities;
    }
}
