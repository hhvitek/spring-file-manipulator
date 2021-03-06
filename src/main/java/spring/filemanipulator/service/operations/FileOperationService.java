package spring.filemanipulator.service.operations;

import spring.filemanipulator.service.entity.operation.file.FileOperationServiceEntity;

import java.util.Collection;

public interface FileOperationService {

    Collection<FileOperationServiceEntity> getAll();
    Collection<String> getAllUniqueNameIds();
    boolean existsByUniqueNameId(String uniqueNameId);
}