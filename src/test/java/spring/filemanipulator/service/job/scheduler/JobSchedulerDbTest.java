package spring.filemanipulator.service.job.scheduler;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.JobStatusEnum;
import spring.filemanipulator.utilities.long_computing.ComputingDelayer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SpringBootTest
// turn off default in-memory autoconfiguration, force to use .yml settings:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobSchedulerDbTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobScheduler jobScheduler;

    private ConditionFactory createAwaitility() {
        return Awaitility.await()
                .pollInterval(10L, TimeUnit.MILLISECONDS)
                .pollDelay(Duration.ZERO)
                .atMost(Duration.ofMillis(100L));
    }


    /**
     * Time-dependent test
     * If fails checks which row and try modifing delays
     *
     * To much failing
     */
    @Disabled
    @Test
    public void scheduleJobThanStopJobStatusShouldRemainStopTest() {
        TestBackgroundJob testJob = new TestBackgroundJob(50);
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

        new ComputingDelayer(50).compute();

        jobEntity = jobRepository.findById(jobEntity.getId()).get();
        Assertions.assertTrue(jobEntity.getJobStatusUniqueNameId() == JobStatusEnum.SIGNALED_TO_STOP);
    }
}