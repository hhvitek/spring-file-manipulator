package spring.filemanipulator.service.task.manager.taskjob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.task.manager.TaskJobFactory;
import spring.filemanipulator.utilities.file_locators.IFileLocator;

/**<pre>
 * TaskJobImpl creator - instance creator
 * TaskJobImpl require
 *  1] IFileLocator to search file system for files.
 *  2] Spring ApplicationEventPublisher so TaskJob can produce events.
 *
 * TaskJob must be unique for every background worker thread,
 * so it could easily use its methods and instance attributes...
 *
 * This is achieved using:
 *   1] Springs ObjectProvider when creating objects AND
 *   2] @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) annotation of new instances
 *      This annotation indicates that ObjectProvider should always instantiate a new instance NOT
 *      provide just one for all invocations...
 *</pre>
 */
@Slf4j
@Service
public class TaskJobFactoryImpl implements TaskJobFactory {

    // task require to search for files
    private final IFileLocator fileLocator;

    // task generates events for its observers
    private final ApplicationEventPublisher eventPublisher;

    // task require its own instance - thread safety
    private final ObjectProvider<TaskJobFileProcessor> objectProviderForTaskJobFileProcessor;

    @Autowired
    protected TaskJobFactoryImpl(final IFileLocator fileLocator1,
                                 final ApplicationEventPublisher eventPublisher1,
                                 final ObjectProvider<TaskJobFileProcessor> objectProviderForTaskJobFileProcessor) {
        this.fileLocator = fileLocator1;
        this.eventPublisher = eventPublisher1;
        this.objectProviderForTaskJobFileProcessor = objectProviderForTaskJobFileProcessor;
    }

    @Override
    public TaskJobImpl createTaskJobByTaskEntity(TaskEntity taskEntity) {
        return new TaskJobImpl(taskEntity, fileLocator, eventPublisher, createNewTaskJobFileProcessorInstance());
    }

    // TODO BEAN exception
    private TaskJobFileProcessor createNewTaskJobFileProcessorInstance() {
        return objectProviderForTaskJobFileProcessor.getIfAvailable();
    }
}