package spring.filemanipulator.service.task.status;

public enum TaskStatusEnum {
    CREATED,
    SCHEDULED,
    RUNNING,
    FINISHED_STOPPED,
    FINISHED_OK,
    FINISHED_PARTIALLY_ERROR,
    FINISHED_ERROR
}