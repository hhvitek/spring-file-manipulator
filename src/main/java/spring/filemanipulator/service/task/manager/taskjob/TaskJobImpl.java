package spring.filemanipulator.service.task.manager.taskjob;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ApplicationEventPublisher;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.entity.TaskFileProcessedEntity;
import spring.filemanipulator.service.entity.operation.file.FileOperationException;
import spring.filemanipulator.service.entity.operation.string.StringOperationException;
import spring.filemanipulator.service.job.Job;
import spring.filemanipulator.service.operation.OperationNotFoundException;
import spring.filemanipulator.service.task.manager.TaskJobProcessingException;
import spring.filemanipulator.service.task.status.TaskStatusEnum;
import spring.filemanipulator.utilities.file_locators.IFileLocator;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

import static spring.filemanipulator.service.task.manager.taskjob.TaskJobEvent.TaskJobEventTypeEnum;

/**<pre>
 * This is the Task implementation. As of now the single implementation of the Job interface.
 *
 * Performs File and FileName operations on all files located in a source folder.
 *
 * In the most general use case it takes all files in a source folder and
 * copies them into a destination folder, renaming files on the way (removing whitespace characters).
 *
 * It requires:
 *  - Task's configuration already stored in the TaskEntity object
 *      -> source and destination folder + operations to be performed etc.
 *  - Spring ApplicationEventPublisher
 *      -> This class is basically Observable class in the Observer pattern.
 *  - TaskProcessor which must be its own not shared across multiple threads etc...
 *</pre>
 */
@Slf4j
public class TaskJobImpl implements Job {

    // observable
    private final ApplicationEventPublisher eventPublisher;

    private final IFileLocator fileLocator;

    private final TaskEntity taskEntity;

    private final TaskJobFileProcessor taskJobFileProcessor;

    // must be volatile
    private volatile boolean requestedToStop = false;

    public TaskJobImpl(final TaskEntity taskEntity,
                       final IFileLocator fileLocator,
                       final ApplicationEventPublisher eventPublisher,
                       final TaskJobFileProcessor taskJobFileProcessor) {
        this.taskEntity = taskEntity;
        this.fileLocator = fileLocator;
        this.eventPublisher = eventPublisher;
        this.taskJobFileProcessor = taskJobFileProcessor;

        if (taskEntity.getId() == null || taskEntity.getId() < 1) {
            log.warn("Something is wrong with TaskEntity! Contains no actual id. {}", taskEntity);
            log.warn("Publishing CREATED event. Trying to resolve issue...");
            setTaskStatusAndPublishTaskEvent(TaskStatusEnum.CREATED, TaskJobEventTypeEnum.OTHER_CHANGE);
        }
    }

    @Override
    public TaskEntity start() {
        //set Running status
        log.debug("!!!!TASK {} starting...!!!", taskEntity.getId());
        setTaskStatusAndPublishTaskEvent(TaskStatusEnum.RUNNING, TaskJobEventTypeEnum.STARTED);

        log.debug("Task {} initializing processor...", taskEntity.getId());
        initializeProcessorAndConvertExceptions();

        List<Path> foundFiles = listAllFilesInSourceFolder();
        // set number of total files
        taskEntity.setTotalFileCount(foundFiles.size());
        setTaskStatusAndPublishTaskEvent(TaskStatusEnum.RUNNING, TaskJobEventTypeEnum.OTHER_CHANGE);

        for (Path file : foundFiles) {
            if (requestedToStop) {
                setTaskStatusAndPublishTaskEvent(TaskStatusEnum.FINISHED_STOPPED, TaskJobEventTypeEnum.FINISHED_STOPPED);
                log.debug("Requested to stop prematurely. Stopping... {}", taskEntity);
                return taskEntity;
            }
            processFileAndPublishEvent(file);
        }

        setTaskStatusAndPublishTaskEvent(TaskStatusEnum.FINISHED_OK, TaskJobEventTypeEnum.FINISHED_OK);

        log.debug("The Task is finishing: {}", taskEntity.getId());
        return taskEntity;
    }

    private void setTaskStatusAndPublishTaskEvent(
            TaskStatusEnum statusType,
            TaskJobEvent.TaskJobEventTypeEnum eventType) {
        taskEntity.setTaskStatusUniqueNameId(statusType.toString());
        TaskJobEvent taskJobEvent = new TaskJobEvent(eventType, taskEntity);
        publishEvent(taskJobEvent);
    }

    private void publishEvent(TaskJobEvent taskJobEvent) {
        log.debug("Publishing event: {}", taskJobEvent.toStringShort());
        eventPublisher.publishEvent(taskJobEvent);
    }

    private void initializeProcessorAndConvertExceptions() {
        try {
            taskJobFileProcessor.initializeFromTaskEntity(taskEntity); // may throw...
        } catch (OperationNotFoundException | InvalidPathException ex) {
            taskEntity.setErrorMsgAndHasErrorFlag(
                    ExceptionUtils.getRootCauseMessage(ex)
            );
            log.error("Encountered error during task processor initialization. TaskId={}. Exception={}",
                    taskEntity.getId(),
                    ExceptionUtils.getRootCauseMessage(ex)
            );
            setTaskStatusAndPublishTaskEvent(TaskStatusEnum.FINISHED_ERROR, TaskJobEventTypeEnum.FINISHED_ERROR);
            throw new TaskJobProcessingException(taskEntity.getId(), ex);
        }
    }

    private List<Path> listAllFilesInSourceFolder() {
        Path sourceFolder = Path.of(taskEntity.getFileOperationInputFolder());
        return fileLocator.listAllFiles(sourceFolder);
    }

    private void processFileAndPublishEvent(Path sourceFile) {
        log.debug("Processing source sourceFile: {}", sourceFile.toAbsolutePath());

        TaskFileProcessedEntity entity;
        try {
            Path newDestinationFilePath = taskJobFileProcessor.performStringOperation(sourceFile); // throws
            taskJobFileProcessor.performFileOperation(sourceFile, newDestinationFilePath); // throws
            entity = new TaskFileProcessedEntity(
                    taskEntity,
                    sourceFile.toAbsolutePath().toString(),
                    newDestinationFilePath.toAbsolutePath().toString()
            );
        } catch (StringOperationException | FileOperationException e) {
            entity = new TaskFileProcessedEntity(
                    taskEntity,
                    sourceFile.toAbsolutePath().toString(),
                    null,
                    e.getMessage()
            );
        }

        taskEntity.increaseProcessedFilesByOne();
        fileProcessedPublishEvent(entity);
    }

    private void fileProcessedPublishEvent(TaskFileProcessedEntity entity) {
        TaskJobEvent taskJobEvent = new TaskJobEvent(TaskJobEventTypeEnum.FILE_PROCESSED, taskEntity, entity);
        publishEvent(taskJobEvent);
    }

    @Override
    public void stop() {
        requestedToStop = true;
    }
}