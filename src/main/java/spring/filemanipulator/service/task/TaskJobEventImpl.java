package spring.filemanipulator.service.task;


import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.entity.TaskFileProcessedEntity;

@ToString
@Setter
@AllArgsConstructor
public class TaskJobEventImpl implements TaskEvent {

    public enum TaskJobEventTypeEnum {
        STARTED, // taskEntity
        FILE_PROCESSED, // taskEntity, TaskFileProcessedEntity
        FINISHED_OK, // taskEntity
        FINISHED_STOPPED, // taskEntity
        ERROR_FILE_PROCESSED, // TaskEntity, TaskFileProcessedEntity
        FINISHED_ERROR // taskEntity

    }

    private TaskJobEventTypeEnum type;
    private TaskEntity taskEntity;
    private TaskFileProcessedEntity taskFileProcessedEntity;

    public TaskJobEventImpl(TaskJobEventTypeEnum type, TaskEntity taskEntity) {
        this.type = type;
        this.taskEntity = taskEntity;
    }

    @Override
    public String getEventName() {
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