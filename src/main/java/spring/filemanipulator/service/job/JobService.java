package spring.filemanipulator.service.job;

import spring.filemanipulator.entity.JobEntity;

/**
 * Manages Job database interactions.
 */
public interface JobService {
    /**
     * Creates and stores a new instance of scheduled Job.
     * Generates ID and an initial status.
     */
    JobEntity createAndSchedule(Job job);

    /**
     * Is running or soon to be...
     * Everything else returns true...
     */
    boolean isFinished(int jobId) throws JobNotFoundException;

    /**
     * If the job has already finished, throws
     */
    void signalToStopIfNotFoundThrow(int jobId) throws JobNotFoundException, JobAlreadyFinishedException;
}