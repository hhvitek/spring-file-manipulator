package spring.filemanipulator.service.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.service.entity.operation.file.FileOperationServiceEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileOperationServiceImpl implements FileOperationService {

    private final List<FileOperationServiceEntity> fileOperationServiceEntities;

    @Autowired
    public FileOperationServiceImpl(final List<FileOperationServiceEntity> fileOperationServiceEntities) {
        this.fileOperationServiceEntities = fileOperationServiceEntities;
    }

    @Override
    public Collection<FileOperationServiceEntity> getAll() {
        return fileOperationServiceEntities;
    }

    @Override
    public Collection<String> getAllUniqueNameIds() {
        return getAll().stream()
                .map(FileOperationServiceEntity::getUniqueNameId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existsByUniqueNameId(String uniqueNameId) {
        return fileOperationServiceEntities.stream()
                   .anyMatch(entity -> entity.getUniqueNameId().equalsIgnoreCase(uniqueNameId));
    }
}