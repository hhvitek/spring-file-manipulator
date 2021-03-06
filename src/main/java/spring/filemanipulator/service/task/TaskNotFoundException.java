package spring.filemanipulator.service.task;

import spring.filemanipulator.service.job.JobNotFoundException;


public class TaskNotFoundException extends RuntimeException {

    private final int id;

    public TaskNotFoundException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "The task (id=" + id + ") not found.";
    }

    @Override
    public String getMessage() {
        return toString();
    }
}