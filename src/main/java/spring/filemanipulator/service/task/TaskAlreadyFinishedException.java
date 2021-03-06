package spring.filemanipulator.service.task;

public class TaskAlreadyFinishedException extends RuntimeException {

    private final int id;

    public TaskAlreadyFinishedException(int id) {
        this.id = id;
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