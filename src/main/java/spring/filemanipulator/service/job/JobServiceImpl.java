package spring.filemanipulator.service.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.scheduler.JobScheduler;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final JobScheduler jobScheduler;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, JobScheduler jobScheduler) {
        this.jobRepository = jobRepository;
        this.jobScheduler = jobScheduler;
    }

    @Override
    public JobEntity createAndSchedule(Job job) {
        JobEntity newJobEntity = storeJobToDbGenerateId();
        jobScheduler.scheduleAndStore(newJobEntity, job);
        return newJobEntity;
    }

    @Override
    public boolean existsRecord(int jobId) {
        return jobRepository.existsById(jobId);
    }

    private JobEntity storeJobToDbGenerateId() {
        JobEntity newJobEntity = JobEntity.createNewWithoutId();
        return jobRepository.save(newJobEntity); // stores to DB and generates id
    }

    @Override
    public boolean isFinishedIfNotFoundThrow(int jobId) throws JobNotFoundException {
        JobEntity jobEntity = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        JobStatusEnum jobStatusEnum = jobEntity.getJobStatusUniqueNameId();
        return jobStatusEnum.isConsideredFinished();
    }

    @Override
    public void signalToStopIfNotFoundThrow(int jobId) throws JobNotFoundException, JobAlreadyFinishedException {
        if (!jobRepository.existsById(jobId)) {
            throw new JobNotFoundException(jobId);
        } else if (jobScheduler.isCreatedOrScheduledOrRunning(jobId)){
            jobScheduler.signalToStop(jobId); // if just deleted, it does nothing...
        } else {
            throw  new JobAlreadyFinishedException(jobId);
        }
    }
}