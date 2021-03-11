package spring.filemanipulator.service.operation.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.service.entity.operation.file.AbstractFileOperationI18nNamedServiceEntity;
import spring.filemanipulator.service.operation.AbstractOperationService;

import java.util.List;

@Service
public class FileOperationServiceImpl
        extends AbstractOperationService<AbstractFileOperationI18nNamedServiceEntity>
        implements FileOperationService {

    @Autowired
    public FileOperationServiceImpl(final List<AbstractFileOperationI18nNamedServiceEntity> fileOperationServiceEntities) {
        super(fileOperationServiceEntities);
    }
}