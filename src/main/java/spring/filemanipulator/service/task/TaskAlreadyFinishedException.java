package spring.filemanipulator.service.task;

import spring.filemanipulator.service.job.JobAlreadyFinishedException;

public class TaskAlreadyFinishedException extends JobAlreadyFinishedException {

    public TaskAlreadyFinishedException(int id) {
        super(id);
    }

    @Override
    public String toString() {
        return "The task (id=" + id + ") has already finished.";
    }

    @Override
    public String getMessage() {
        return toString();
    }
}