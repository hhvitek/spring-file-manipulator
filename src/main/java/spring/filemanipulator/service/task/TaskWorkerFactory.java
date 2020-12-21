package spring.filemanipulator.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.job.Worker;
import spring.filemanipulator.utilities.file_locators.IFileLocator;

@Service
public class TaskWorkerFactory {

    private final IFileLocator fileLocator;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    protected TaskWorkerFactory(final IFileLocator fileLocator1,
                                final ApplicationEventPublisher eventPublisher1) {
        this.fileLocator = fileLocator1;
        this.eventPublisher = eventPublisher1;
    }

    public Worker getTaskWorker(TaskEntity taskEntity) {
        return new TaskWorkerImpl(taskEntity, fileLocator, eventPublisher);
    }
}
