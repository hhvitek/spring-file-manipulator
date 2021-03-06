package spring.filemanipulator.service.task.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.entity.TaskFileProcessedEntity;
import spring.filemanipulator.service.job.Job;
import spring.filemanipulator.service.task.TaskEvent;
import spring.filemanipulator.service.task.TaskJobEventImpl;
import spring.filemanipulator.service.task.status.TaskStatusEnum;
import spring.filemanipulator.utilities.file_locators.IFileLocator;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static spring.filemanipulator.service.task.TaskJobEventImpl.TaskJobEventTypeEnum.*;

@Slf4j
public class TaskJobImpl implements Job {

    private final ApplicationEventPublisher eventPublisher;

    private final IFileLocator fileLocator;

    private final TaskEntity taskEntity;

    // must be volatile
    private volatile boolean requestedToStop = false;

    public TaskJobImpl(TaskEntity taskEntity,
                       final IFileLocator fileLocator,
                       final ApplicationEventPublisher eventPublisher) {
        this.taskEntity = taskEntity;
        this.fileLocator = fileLocator;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public TaskEntity start() {
        //set Running status
        setStatusAndPublishEvent(TaskStatusEnum.RUNNING, STARTED);

        List<Path> foundFiles = listAllFilesInSourceFolder();

        // set number of total files
        int filesCount = foundFiles.size();
        taskEntity.setTotalFileCount(filesCount);

        for (Path file : foundFiles) {
            if (requestedToStop) {
                setStatusAndPublishEvent(TaskStatusEnum.FINISHED_STOPPED, FINISHED_STOPPED);
                log.info("Requested to stop prematurely. Stopping... {}", taskEntity);
                return taskEntity;
            }
            processFile(file);
        }

        setStatusAndPublishEvent(TaskStatusEnum.FINISHED_OK, FINISHED_OK);

        return taskEntity;
    }

    private void setStatusAndPublishEvent(
            TaskStatusEnum statusType,
            TaskJobEventImpl.TaskJobEventTypeEnum eventType) {
        taskEntity.setTaskStatusUniqueNameId(statusType.toString());
        TaskEvent jobEvent = new TaskJobEventImpl(eventType, taskEntity);
        publishEvent(jobEvent);
    }

    private void publishEvent(TaskEvent jobEvent) {
        log.info("Publishing event: {}", jobEvent);
        eventPublisher.publishEvent(jobEvent);
    }

    private List<Path> listAllFilesInSourceFolder() {
        Path sourceFolder = Path.of(taskEntity.getFileOperationInputFolder());
        return fileLocator.listAllFiles(sourceFolder);
    }

    private void processFile(Path file) {
        log.info("Processing source file: {}", file.toAbsolutePath());

        taskEntity.increaseProcessedFilesByOne();
        TaskFileProcessedEntity entity = new TaskFileProcessedEntity(taskEntity);
        fileProcessedPublishEvent(entity);

        BigInteger veryBig = new BigInteger(5000, new Random());
        veryBig.nextProbablePrime();

        // source file
        // get filename
        // transform filename to destination filename
        // perform operation with source file and transformed destination filename
    }

    private void fileProcessedPublishEvent(TaskFileProcessedEntity entity) {
        TaskEvent jobEvent = new TaskJobEventImpl(FILE_PROCESSED, taskEntity, entity);
        publishEvent(jobEvent);
    }

    @Override
    public void stop() {
        requestedToStop = true;
    }
}