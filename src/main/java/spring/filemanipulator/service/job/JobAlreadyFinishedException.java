package spring.filemanipulator.service.job;

public class JobAlreadyFinishedException extends RuntimeException {

    protected final int id;

    public JobAlreadyFinishedException(int jobId) {
        this.id = jobId;
    }

    @Override
    public String toString() {
        return "The Job (id=" + id + ") has already finished.";
    }

    @Override
    public String getMessage() {
        return toString();
    }

}