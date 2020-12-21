package spring.filemanipulator.service.job;

public interface WorkerManager {

    /**
     * Returns unique worker id, used to identify the scheduled worker.
     */
    int schedule(Worker worker);

    boolean contains(int workerId);

    /**
     * First try to stop worker gracefully using worker's stop() method.
     */
    void stop(int workerId) throws WorkerNotFoundException;
}
