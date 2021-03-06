package spring.filemanipulator.service.job;

import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.scheduler.JobScheduler;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final JobScheduler jobScheduler;

    public JobServiceImpl(JobRepository jobRepository, JobScheduler jobScheduler) {
        this.jobRepository = jobRepository;
        this.jobScheduler = jobScheduler;
    }

    @Override
    public JobEntity createAndSchedule(Job job) {
        JobEntity newJobEntity = storeJobToDbGenerateId(job);
        jobScheduler.scheduleAndStore(newJobEntity, job);
        return newJobEntity;
    }

    private JobEntity storeJobToDbGenerateId(Job job) {
        JobEntity newJobEntity = JobEntity.createNewWithoutId();
        return jobRepository.save(newJobEntity); // stores to DB and generates id
    }

    @Override
    public boolean isFinished(int jobId) throws JobNotFoundException {
        JobEntity jobEntity = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        JobStatusEnum jobStatusEnum = jobEntity.getJobStatusUniqueNameId();
        return jobStatusEnum.isConsideredFinished();
    }

    @Override
    public void signalToStopIfNotFoundThrow(int jobId) throws JobNotFoundException, JobAlreadyFinishedException {
        if (!jobRepository.existsById(jobId)) {
            throw new JobNotFoundException(jobId);
        } else if (!jobScheduler.isScheduledOrRunning(jobId)){
            throw new JobAlreadyFinishedException(jobId);
        } else {
            jobScheduler.signalToStop(jobId); // if just deleted, it does nothing...
        }
    }
}