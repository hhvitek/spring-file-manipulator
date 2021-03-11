package spring.filemanipulator.service.task;

import spring.filemanipulator.service.job.JobNotScheduledException;

public class TaskNotScheduledException extends JobNotScheduledException {

    public TaskNotScheduledException(int id) {
        super(id);
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