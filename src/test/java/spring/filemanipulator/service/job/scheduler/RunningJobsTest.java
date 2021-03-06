package spring.filemanipulator.service.job.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.filemanipulator.service.job.Job;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class RunningJobsTest {

    private final RunningJobs runningJobs;

    public RunningJobsTest() {
        runningJobs = new RunningJobs();

    }

    private class CustomInnerJob implements Job {
        public boolean calledStart = false;
        public boolean calledStop = false;

        @Override
        public Object start() {
            calledStart = true;
            return calledStart;
        }

        @Override
        public void stop() {
            calledStop = true;
        }
    }

    @Test
    public void callingSetFutureOnNonExistentJobDoesNothingTest() {

        int jobId = 99;

        Assertions.assertFalse(runningJobs.contains(99));

        runningJobs.setFuture(jobId, generateCompletableFuture());

        Assertions.assertFalse(runningJobs.contains(99));
    }

    private CompletableFuture<Object> generateCompletableFuture() {
        return CompletableFuture.supplyAsync(
                () -> { return false; }
        );
    }

    @Test
    public void callingAddNewCreatesRunningJobTest() {
        int jobId = 99;

        Assertions.assertFalse(runningJobs.contains(99));

        runningJobs.addNew(jobId, new CustomInnerJob());

        Assertions.assertTrue(runningJobs.contains(99));
    }

    @Test
    public void deleteNotFoundShouldDoNothingTest() {
        int jobId = 99;

        Assertions.assertFalse(runningJobs.contains(99));

        Assertions.assertDoesNotThrow( () -> runningJobs.delete(jobId));

        Assertions.assertFalse(runningJobs.contains(99));
    }

    @Test
    public void stoppingRunningJobWithoutFutureShouldDoNothingTest() {
        int jobId = 99;

        CustomInnerJob customInnerJob = new CustomInnerJob();
        runningJobs.addNew(jobId, customInnerJob);

        Assertions.assertDoesNotThrow( () -> runningJobs.stop(jobId) );

        Assertions.assertFalse(customInnerJob.calledStop);
    }

    @Test
    public void stoppingRunningJobWithFutureShouldCallStopMethodTest() {
        int jobId = 99;

        CustomInnerJob customInnerJob = new CustomInnerJob();
        runningJobs.addNew(jobId, customInnerJob);
        runningJobs.setFuture(jobId, generateCompletableFuture());

        Assertions.assertDoesNotThrow( () -> runningJobs.stop(jobId) );

        Assertions.assertTrue(customInnerJob.calledStop);
    }

}