package spring.filemanipulator.service.task.manager;

import lombok.Getter;

@Getter
public class TaskJobProcessingException extends RuntimeException {

    private int id = -1;

    public TaskJobProcessingException(int id, Throwable throwable) {
        super(throwable);
        this.id = id;
    }

    public TaskJobProcessingException(String message) {
        super(message);
    }
}