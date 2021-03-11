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
     * Returns true if DB contains record about Job with id = jobId.
     *
     * Basically has there ever been a Job with id = jobId?
     */
    boolean existsRecord(int jobId);

    /**
     * Is running or soon to be...
     * Everything else returns true...
     */
    boolean isFinishedIfNotFoundThrow(int jobId) throws JobNotFoundException;

    /**
     * If the job has already finished, throws
     * If the job (id) does not exist, throws
     */
    void signalToStopIfNotFoundThrow(int jobId) throws JobNotFoundException, JobAlreadyFinishedException;
}