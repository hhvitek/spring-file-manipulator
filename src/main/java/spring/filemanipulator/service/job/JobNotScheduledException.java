package spring.filemanipulator.service.job;

public class JobNotScheduledException extends RuntimeException {

    protected final int id;

    public JobNotScheduledException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "The job (id=" + id + ") has not been scheduled yet.";
    }

    @Override
    public String getMessage() {
        return toString();
    }
}