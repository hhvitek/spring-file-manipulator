package spring.filemanipulator.service.job.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.JobStatusEnum;

@SpringBootTest(classes = {JobSchedulerImpl.class, RunningJobs.class})
class JobSchedulerImplTest {

    @Autowired
    private JobScheduler jobScheduler;

    @MockBean
    private JobRepository jobRepository;

    @Test
    public void scheduledJobFinishesOkTest() throws InterruptedException {
        TestBackgroundJob testJob = new TestBackgroundJob(1);

        JobEntity jobEntity = JobEntity.createNewWithoutId();
        int jobId = 1;
        jobEntity.setId(jobId);

        jobScheduler.scheduleAndStore(jobEntity, testJob);
        // wait
        // test: isScheduledOrRunning, executed start(), jobEntity status

        Assertions.assertTrue(jobScheduler.isCreatedOrScheduledOrRunning(jobId));

        Thread.sleep(20);

        Assertions.assertTrue(testJob.startedExecuted);

        Assertions.assertEquals(JobStatusEnum.FINISHED_OK, jobEntity.getJobStatusUniqueNameId());
        Assertions.assertFalse(jobScheduler.isCreatedOrScheduledOrRunning(jobId));
    }

    @Test
    public void scheduledJobThrowsTest() throws InterruptedException {
        TestBackgroundJob testJob = new TestBackgroundJob(true);

        JobEntity jobEntity = JobEntity.createNewWithoutId();
        int jobId = 1;
        jobEntity.setId(jobId);

        jobScheduler.scheduleAndStore(jobEntity, testJob);
        // wait
        // test: isScheduledOrRunning, executed start(), jobEntity status

        Assertions.assertTrue(jobScheduler.isCreatedOrScheduledOrRunning(jobId));

        Thread.sleep(20);

        Assertions.assertTrue(testJob.startedExecuted);

        Assertions.assertEquals(JobStatusEnum.FINISHED_ERROR, jobEntity.getJobStatusUniqueNameId());
        Assertions.assertFalse(jobScheduler.isCreatedOrScheduledOrRunning(jobId));
    }

    @Test
    public void signalStopToNonExistentJobDoesNothingTest() {

        int jobId = 1;
        Assertions.assertFalse(jobScheduler.isCreatedOrScheduledOrRunning(jobId));

        Assertions.assertDoesNotThrow(() -> jobScheduler.signalToStop(jobId));

        Assertions.assertFalse(jobScheduler.isCreatedOrScheduledOrRunning(jobId));
    }

    @Test
    public void signalStopToRunningJobExecutesStopMethod() {
        TestBackgroundJob testJob = new TestBackgroundJob(100);

        JobEntity jobEntity = JobEntity.createNewWithoutId();
        int jobId = 1;
        jobEntity.setId(jobId);

        jobScheduler.scheduleAndStore(jobEntity, testJob);

        Assertions.assertTrue(jobScheduler.isCreatedOrScheduledOrRunning(jobId));
        Assertions.assertFalse(testJob.stopExecuted);

        jobScheduler.signalToStop(jobId);

        Assertions.assertTrue(testJob.stopExecuted);
    }

}