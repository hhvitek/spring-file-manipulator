package spring.filemanipulator.service.entity.operation.file;

import org.springframework.context.MessageSource;
import spring.filemanipulator.service.entity.AbstractI18nNamedServiceEntity;

public abstract class AbstractFileOperationNamedServiceEntity extends AbstractI18nNamedServiceEntity implements FileOperationServiceEntity {
    private static final String MESSAGES_PATH_PREFIX = "operations.file.";

    protected AbstractFileOperationNamedServiceEntity(final String uniqueNameId, final MessageSource messageSource) {
        super(MESSAGES_PATH_PREFIX, uniqueNameId, messageSource);
    }
}