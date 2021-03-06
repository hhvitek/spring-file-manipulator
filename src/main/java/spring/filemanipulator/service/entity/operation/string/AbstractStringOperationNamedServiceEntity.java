package spring.filemanipulator.service.entity.operation.string;

import lombok.Setter;
import org.springframework.context.MessageSource;
import spring.filemanipulator.service.entity.AbstractI18nNamedServiceEntity;

@Setter
public abstract class AbstractStringOperationNamedServiceEntity extends AbstractI18nNamedServiceEntity {

    private static final String MESSAGES_PATH_PREFIX = "operations.string.";

    protected String filterWhat;

    protected String replaceWith;

    protected AbstractStringOperationNamedServiceEntity(final String uniqueNameId, final MessageSource messageSource) {
        super(MESSAGES_PATH_PREFIX, uniqueNameId, messageSource);
    }
}