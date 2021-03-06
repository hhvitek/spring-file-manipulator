package spring.filemanipulator.service.task.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.utilities.file_locators.IFileLocator;

@Service
public class TaskJobFactoryImpl {

    private final IFileLocator fileLocator;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    protected TaskJobFactoryImpl(final IFileLocator fileLocator1,
                                 final ApplicationEventPublisher eventPublisher1) {
        this.fileLocator = fileLocator1;
        this.eventPublisher = eventPublisher1;
    }

    public TaskJobImpl createTaskJobByTaskEntity(TaskEntity taskEntity) {
        return new TaskJobImpl(taskEntity, fileLocator, eventPublisher);
    }
}