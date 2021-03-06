package spring.filemanipulator.service.entity.task;

import org.springframework.context.MessageSource;
import spring.filemanipulator.service.entity.AbstractI18nNamedServiceEntity;

public class TaskStatusI18nNameServiceEntity extends AbstractI18nNamedServiceEntity {

    private static final String MESSAGES_PATH_PREFIX = "task.status.";

    public TaskStatusI18nNameServiceEntity(String uniqueNameId, final MessageSource messageSource) {
        super(MESSAGES_PATH_PREFIX, uniqueNameId, messageSource);
    }
}