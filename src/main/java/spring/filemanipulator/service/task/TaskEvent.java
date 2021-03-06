package spring.filemanipulator.service.task;

public interface TaskEvent {
    String getEventName();
    Object firstValue();
    Object secondValue();
}