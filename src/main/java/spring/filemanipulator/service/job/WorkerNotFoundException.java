package spring.filemanipulator.service.job;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException(String s) {
        super(s);
    }
}
