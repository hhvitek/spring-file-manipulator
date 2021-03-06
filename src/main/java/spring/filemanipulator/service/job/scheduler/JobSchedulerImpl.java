package spring.filemanipulator.service.job.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.Job;
import spring.filemanipulator.service.job.JobStatusEnum;

import java.util.concurrent.CompletableFuture;

// TODO explicitly saving entity
@Slf4j
@Service
public class JobSchedulerImpl implements JobScheduler {

    // every SCHEDULED_REPEATABLE_DELAY_IN_MILLIS execute async repeatable method in this object.
    private static final int SCHEDULED_REPEATABLE_DELAY_IN_MILLIS = 5000;

    private final RunningJobs runningJobs;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final JobRepository jobRepository;

    @Autowired
    public JobSchedulerImpl(final ThreadPoolTaskScheduler threadPoolTaskScheduler,
                            final RunningJobs runningJobs,
                            final JobRepository jobRepository) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.runningJobs = runningJobs;
        this.jobRepository = jobRepository;

        setTaskSchedulerPoolToNumberOfProcessors(threadPoolTaskScheduler);

        //Runnable scheduledRepeatedTaskRunnable = this::scheduledRepeatedTask;
        //threadPoolTaskScheduler.scheduleWithFixedDelay(scheduledRepeatedTaskRunnable, SCHEDULED_REPEATABLE_DELAY_IN_MILLIS);
    }

    private void setTaskSchedulerPoolToNumberOfProcessors(ThreadPoolTaskScheduler taskScheduler) {
        int cpus = Runtime.getRuntime().availableProcessors();
        log.info("Number of cpus: {}", cpus);
        taskScheduler.setPoolSize(cpus);
    }

    @Override
    public void scheduleAndStore(JobEntity jobEntity, Job job) {
        runningJobs.addNew(jobEntity.getId(), job);

        CompletableFuture<Object> completableFuture = createFutureAndScheduleJob(jobEntity, job);

        // hmm, theoretically this code "can" be executed before the lines above:
        // soouu let's just ignore it.
        // update: - reworked runningJobs, no exceptions
        //         - so fast that delete called sooner than add, may result in leak -> reworked runningJobs more ->
        //              -> split runningJobs into two phases, here come the second one:
        runningJobs.setFuture(jobEntity.getId(), completableFuture);
    }

    private CompletableFuture<Object> createFutureAndScheduleJob(JobEntity jobEntity, Job job) {
        return CompletableFuture
                .supplyAsync(() -> {
                    jobEntity.setJobStatusUniqueNameId(JobStatusEnum.SCHEDULED_RUNNING);
                    return job.start();
                })
                .whenCompleteAsync((result, throwable) -> {
                    runningJobs.delete(jobEntity.getId());

                    if (didCompleteWithException(throwable)) {
                        handleJobException(throwable, jobEntity);
                    } else {
                        handleJobResult(result, jobEntity);
                    }
                }, threadPoolTaskScheduler);
    }

    private boolean didCompleteWithException(Throwable throwable) {
        return throwable != null;
    }

    private void handleJobException(Throwable throwable, JobEntity jobEntity) {
        log.warn("Job Exception thrown!!!!!!! {}, jobEntity: {}", throwable, jobEntity);
        jobEntity.setJobStatusUniqueNameId(JobStatusEnum.FINISHED_ERROR);
    }

    private void handleJobResult(Object jobResult, JobEntity jobEntity) {
        log.warn("Job result: {}, jobEntity: {}", jobResult, jobEntity);
        jobEntity.setJobStatusUniqueNameId(JobStatusEnum.FINISHED_OK);
    }

    @Override
    public boolean isScheduledOrRunning(int jobId) {
        return runningJobs.contains(jobId);
    }

    @Override
    public void signalToStop(int jobId) {
        if (isScheduledOrRunning(jobId)) {
            runningJobs.stop(jobId);
            // Does it to make sense to set Job status to STOP here
            // even throughout a Job can run for hours long before it stops itself?
            // otherwise a Job implementation would have to do it...
            setJobStatusToStopped(jobId);
        }

    }

    private void setJobStatusToStopped(int jobId) {
        if (jobRepository.existsById(jobId)) {
            JobEntity jobEntity = jobRepository.findById(jobId).get();
            jobEntity.setJobStatusUniqueNameId(JobStatusEnum.SIGNALED_TO_STOP);
        }
    }

    /**
     * Repeatable scheduled task, anything???
     */
    private void scheduledRepeatedTask() {
        log.info("Scheduled Tasks repeatable thread executed!!!! Nothing happened...");

        /*
          synchronized (workerIdToOneWorkerItem) {
            for (Map.Entry<Integer, ScheduledJobImpl> entry: workerIdToOneWorkerItem.entrySet()) {
                Integer workerId = entry.getKey();
                ScheduledJobImpl item = entry.getValue();

                // do something
            }
        }
        */
    }
}