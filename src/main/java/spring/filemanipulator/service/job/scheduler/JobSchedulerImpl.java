package spring.filemanipulator.service.job.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.Job;
import spring.filemanipulator.service.job.JobStatusEnum;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class JobSchedulerImpl implements JobScheduler {

    // every SCHEDULED_REPEATABLE_DELAY_IN_MILLIS execute async repeatable method in this object.
    private static final int SCHEDULED_REPEATABLE_DELAY_IN_MILLIS = 5000;

    private final RunningJobs runningJobs;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final JobRepository jobRepository;

    @Autowired
    public JobSchedulerImpl(final RunningJobs runningJobs,
                            final JobRepository jobRepository) {
        this.runningJobs = runningJobs;
        this.jobRepository = jobRepository;

        threadPoolTaskExecutor = getThreadPoolTaskExecutor();
    }

    /**
     * Let's explain ThreadPoolTaskExecutor
     * corePoolSize - Default 1. If FULL then it prefers to queue new workers.
     * maxPoolSize
     * queueCapacity - Default INFINITY. If FULL And corePoolSize FULL than new workers up to maxPoolSize.
     *
     * This means that maxPoolSize is mostly irrelevant if configured alone!!!
     */
    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int cpus = Runtime.getRuntime().availableProcessors();
        log.debug("jobScheduler: Number of cpus: {}", cpus);

        executor.setCorePoolSize(cpus);
        executor.setMaxPoolSize(cpus);
        executor.setThreadNamePrefix("jobScheduler-");
        executor.initialize();

        log.debug("jobScheduler: corePoolSize-{}, maxPoolSize-{}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize());

        return executor;
    }

    @Override
    public void scheduleAndStore(JobEntity jobEntity, Job job) {
        runningJobs.addNew(jobEntity.getId(), job);

        log.debug("JobScheduler Job Starting scheduling...");
        CompletableFuture<Object> completableFuture = createFutureAndScheduleJob(jobEntity, job);

        runningJobs.setFuture(jobEntity.getId(), completableFuture);
        log.debug("JobScheduler finished scheduling...");
    }

    private CompletableFuture<Object> createFutureAndScheduleJob(JobEntity jobEntity, Job job) {
        return CompletableFuture
                .supplyAsync(() -> {
                    setNewJobStatusAndStoreIntoDb(jobEntity, JobStatusEnum.SCHEDULED_RUNNING);

                    return job.start();
                }, threadPoolTaskExecutor)
                .whenCompleteAsync((result, throwable) -> {
                    runningJobs.delete(jobEntity.getId());

                    if (didCompleteWithException(throwable)) {
                        handleJobException(throwable, jobEntity);
                    } else {
                        handleJobResult(result, jobEntity);
                    }
                }, threadPoolTaskExecutor);
    }

    private void setNewJobStatusAndStoreIntoDb(JobEntity jobEntity, JobStatusEnum newJobStatus) {
        jobEntity.setJobStatusUniqueNameId(newJobStatus);
        jobRepository.save(jobEntity);
    }

    private boolean didCompleteWithException(Throwable throwable) {
        return throwable != null;
    }

    private void handleJobException(Throwable throwable, JobEntity jobEntity) {
        log.error("JobScheduler Exception thrown!!!!!!! {}, jobEntity: {}", throwable, jobEntity);
        setNewJobStatusAndStoreIntoDb(jobEntity, JobStatusEnum.FINISHED_ERROR);
    }

    private void handleJobResult(Object jobResult, JobEntity jobEntity) {
        log.debug("JobScheduler finished ok result: {}, jobEntity: {}", jobResult, jobEntity);

        // TODO what if job was stopped
        // should I cancel completableFuture hardcore
        // should I place here if-else on current status...
        if (wasJobStopped(jobEntity.getId())) {
            log.debug("JobScheduler job {} was stopped before. Ignoring...", jobEntity.getId());
        } else {
            setNewJobStatusAndStoreIntoDb(jobEntity, JobStatusEnum.FINISHED_OK);
        }

    }

    private boolean wasJobStopped(int jobId) {
        return jobRepository.findById(jobId)
                .stream()
                .anyMatch(jobEntity -> jobEntity.getJobStatusUniqueNameId().isConsideredStopped());
    }

    @Override
    public boolean isCreatedOrScheduledOrRunning(int jobId) {
        return runningJobs.contains(jobId);
    }

    @Override
    public void signalToStop(int jobId) {
        if (isCreatedOrScheduledOrRunning(jobId)) {
            runningJobs.stop(jobId);
            runningJobs.delete(jobId);
            // Does it to make sense to set Job status to STOP here
            // even throughout a Job can run for hours long before it stops itself?
            // otherwise a Job implementation would have to do it...
            // maybe yes, after Job finishes ther should be check...
            setJobStatusToStopped(jobId);
        }

    }

    private void setJobStatusToStopped(int jobId) {
        if (jobRepository.existsById(jobId)) {
            JobEntity jobEntity = jobRepository.findById(jobId).get();

            setNewJobStatusAndStoreIntoDb(jobEntity, JobStatusEnum.SIGNALED_TO_STOP);
        }
    }
}