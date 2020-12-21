package spring.filemanipulator.service.job;

public interface WorkerEvent {
    String getName();
    Object firstValue();
    Object secondValue();
}
