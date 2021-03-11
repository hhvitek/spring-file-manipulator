package spring.filemanipulator.service.job.scheduler;

import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.service.job.Job;

/**
 * Class used to schedule and execute Job instances in background threads.
 */
public interface JobScheduler {
    /**
     * This method takes a Job interface instance (implementation)
     * and it schedules it's {@link Job#start() start()} method to be executed ASAP.
     *
     * @param jobEntity Job representation in a database.
     * @param job Job to be scheduled.
     */
    void scheduleAndStore(JobEntity jobEntity, Job job);

    /**
     * Whether or not the Job (jobId) is managed by this JobScheduler.
     * After a Job finishes it's execution, it is silently released from a JobScheduler.
     * Therefore only recently created, scheduled or running Jobs return true...
     */
    boolean isCreatedOrScheduledOrRunning(int jobId);


    /**
     * Try to stop the Job gracefully using job's stop() method.
     * As mentioned in {@link Job#stop}
     * If not found silently ignore
     */
    void signalToStop(int jobId);
}