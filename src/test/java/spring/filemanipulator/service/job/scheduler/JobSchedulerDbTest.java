package spring.filemanipulator.service.job.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.JobStatusEnum;
import spring.filemanipulator.utilities.long_computing.ComputingDelayer;

@SpringBootTest
// turn off default in-memory autoconfiguration, force to use .yml settings:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobSchedulerDbTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobScheduler jobScheduler;

    /**
     * Time-dependent test
     * If fails checks which row and try modifing delays
     */
    @Test
    public void scheduleJobThanStopJobStatusShouldRemainStopTest() {
        TestBackgroundJob testJob = new TestBackgroundJob(75);
        JobEntity jobEntity = JobEntity.createNewWithoutId();
        Assertions.assertTrue(jobEntity.getJobStatusUniqueNameId() == JobStatusEnum.CREATED);

        jobEntity = jobRepository.save(jobEntity);

        jobScheduler.scheduleAndStore(jobEntity, testJob);

        jobEntity = jobRepository.findById(jobEntity.getId()).get();

        Assertions.assertTrue(jobScheduler.isCreatedOrScheduledOrRunning(jobEntity.getId()));
        Assertions.assertTrue(jobEntity.getJobStatusUniqueNameId() == JobStatusEnum.SCHEDULED_RUNNING);

        jobScheduler.signalToStop(jobEntity.getId());

        jobEntity = jobRepository.findById(jobEntity.getId()).get();

        Assertions.assertFalse(jobScheduler.isCreatedOrScheduledOrRunning(jobEntity.getId()));
        Assertions.assertTrue(jobEntity.getJobStatusUniqueNameId() == JobStatusEnum.SIGNALED_TO_STOP);

        new ComputingDelayer(75).compute();

        jobEntity = jobRepository.findById(jobEntity.getId()).get();
        Assertions.assertTrue(jobEntity.getJobStatusUniqueNameId() == JobStatusEnum.SIGNALED_TO_STOP);
    }
}