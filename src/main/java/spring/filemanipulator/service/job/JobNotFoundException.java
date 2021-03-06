package spring.filemanipulator.service.job;

public class JobNotFoundException extends RuntimeException {

    private final int id;

    public JobNotFoundException(int jobId) {
        this.id = jobId;
    }

    @Override
    public String toString() {
        return "The Job (id=" + id + ") not found.";
    }
}