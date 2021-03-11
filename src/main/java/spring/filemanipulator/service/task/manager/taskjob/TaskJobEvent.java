package spring.filemanipulator.service.task.manager.taskjob;


import lombok.*;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.entity.TaskFileProcessedEntity;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class TaskJobEvent {

    public enum TaskJobEventTypeEnum {
        STARTED, // taskEntity
        OTHER_CHANGE, // taskEntity
        FILE_PROCESSED, // taskEntity, TaskFileProcessedEntity
        FINISHED_OK, // taskEntity
        FINISHED_STOPPED, // taskEntity
        ERROR_FILE_PROCESSED, // TaskEntity, TaskFileProcessedEntity
        FINISHED_ERROR // taskEntity

    }

    private TaskJobEventTypeEnum type;
    private TaskEntity taskEntity;
    private TaskFileProcessedEntity taskFileProcessedEntity;

    public TaskJobEvent(TaskJobEventTypeEnum type, TaskEntity taskEntity) {
        this.type = type;
        this.taskEntity = taskEntity;
    }

    public boolean isFileProcessedEvent() {
        return type == TaskJobEventTypeEnum.FILE_PROCESSED;
    }

    public String toStringShort() {
        String message = type.toString() + ": taskId=" + taskEntity.getId();

        if (taskFileProcessedEntity != null) {
            message += " | sourceFile=" + taskFileProcessedEntity.getSourceFile();
        }

        return message;
    }
}