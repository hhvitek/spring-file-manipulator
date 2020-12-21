package spring.filemanipulator.service.entity.operation.file;

import org.springframework.context.MessageSource;
import spring.filemanipulator.service.entity.AbstractNamedServiceEntity;

public abstract class AbstractFileOperationNamedServiceEntity extends AbstractNamedServiceEntity implements FileOperationServiceEntity {
    private static final String MESSAGES_PATH_PREFIX = "operations.file.";

    protected AbstractFileOperationNamedServiceEntity(final String uniqueNameId, final MessageSource messageSource) {
        super(MESSAGES_PATH_PREFIX, uniqueNameId, messageSource);
    }
}
