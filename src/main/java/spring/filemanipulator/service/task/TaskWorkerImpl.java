package spring.filemanipulator.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.entity.TaskFileProcessedEntity;
import spring.filemanipulator.service.job.Worker;
import spring.filemanipulator.service.job.WorkerEvent;
import spring.filemanipulator.utilities.file_locators.IFileLocator;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static spring.filemanipulator.service.task.TaskWorkerEvent.TaskWorkerEventEnum.*;

@Slf4j
public class TaskWorkerImpl implements Worker {

    private final ApplicationEventPublisher eventPublisher;

    private final IFileLocator fileLocator;

    private TaskEntity taskEntity;

    // must be volatile
    private volatile boolean requestedToStop = false;

    public TaskWorkerImpl(TaskEntity taskEntity,
                          final IFileLocator fileLocator,
                          final ApplicationEventPublisher eventPublisher) {
        this.taskEntity = taskEntity;
        this.fileLocator = fileLocator;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public TaskEntity start() {
        //set Running status
        setStatusAndPublishEvent(TaskStatusType.RUNNING, STARTED);

        Path sourceFolder = Path.of(taskEntity.getFileOperationInputFolder());
        List<Path> foundFiles = fileLocator.listAllFiles(sourceFolder);

        // set number of total files
        int filesCount = foundFiles.size();
        taskEntity.setTotalFileCount(filesCount);

        for(Path file: foundFiles) {
            if (requestedToStop) {
                setStatusAndPublishEvent(TaskStatusType.FINISHED_CANCELLED, STOPPED);
                return taskEntity;
            }
            log.info(file.toString());
            taskEntity.increaseProcessedFilesByOne();
            TaskFileProcessedEntity entity = new TaskFileProcessedEntity();
            fileProcessedPublishEvent(entity);

            BigInteger veryBig = new BigInteger(2000, new Random());
            veryBig.nextProbablePrime();
        }

        setStatusAndPublishEvent(TaskStatusType.FINISHED_OK, FINISHED);

        return taskEntity;
    }

    private void setStatusAndPublishEvent(
            TaskStatusType statusType,
            TaskWorkerEvent.TaskWorkerEventEnum eventType) {
        taskEntity.setTaskStatusUniqueNameId(statusType.toString());
        WorkerEvent workerEvent = new TaskWorkerEvent(eventType, taskEntity);
        publishEvent(workerEvent);
    }

    private void fileProcessedPublishEvent(TaskFileProcessedEntity entity) {
        WorkerEvent workerEvent = new TaskWorkerEvent(FILE_PROCESSED, taskEntity, entity);
        publishEvent(workerEvent);
    }

    private void publishEvent(WorkerEvent workerEvent) {
        log.info("Publishing event: {}", workerEvent);
        eventPublisher.publishEvent(workerEvent);
    }

    @Override
    public void stop() {
        requestedToStop = true;
    }
}
