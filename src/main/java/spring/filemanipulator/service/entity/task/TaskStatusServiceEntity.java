package spring.filemanipulator.service.entity.task;

import org.springframework.context.MessageSource;
import spring.filemanipulator.service.entity.AbstractNamedServiceEntity;

public class TaskStatusServiceEntity extends AbstractNamedServiceEntity {

    private static final String MESSAGES_PATH_PREFIX = "task.status.";

    public TaskStatusServiceEntity(String uniqueNameId, final MessageSource messageSource) {
        super(MESSAGES_PATH_PREFIX, uniqueNameId, messageSource);
    }
}
