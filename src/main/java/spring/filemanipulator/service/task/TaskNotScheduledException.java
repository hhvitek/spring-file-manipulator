package spring.filemanipulator.service.task;

import spring.filemanipulator.service.task.TaskNotFoundException;

public class TaskNotScheduledException extends RuntimeException {

    private final int id;

    public TaskNotScheduledException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "The task (id=" + id + ") has not been scheduled yet.";
    }

    @Override
    public String getMessage() {
        return toString();
    }
}