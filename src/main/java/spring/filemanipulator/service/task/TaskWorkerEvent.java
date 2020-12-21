package spring.filemanipulator.service.task;


import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.entity.TaskFileProcessedEntity;
import spring.filemanipulator.service.job.WorkerEvent;

@ToString
@Setter
@AllArgsConstructor
public class TaskWorkerEvent implements WorkerEvent {

    public enum TaskWorkerEventEnum {
        STARTED, // taskEntity
        FILE_PROCESSED, // taskEntity, TaskFileProcessedEntity
        FINISHED, // taskEntity
        STOPPED, // taskEntity
        ERROR_FILE_PROCESSED, // TaskEntity, TaskFileProcessedEntity
        ERROR // taskEntity

    }

    private TaskWorkerEventEnum type;
    private TaskEntity taskEntity;
    private TaskFileProcessedEntity taskFileProcessedEntity;

    public TaskWorkerEvent(TaskWorkerEventEnum type, TaskEntity taskEntity) {
        this.type = type;
        this.taskEntity = taskEntity;
    }

    @Override
    public String getName() {
        return type.toString();
    }

    @Override
    public TaskEntity firstValue() {
        return taskEntity;
    }

    @Override
    public TaskFileProcessedEntity secondValue() {
        return taskFileProcessedEntity;
    }
}
