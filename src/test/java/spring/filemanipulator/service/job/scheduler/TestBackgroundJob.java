package spring.filemanipulator.service.job.scheduler;

import lombok.extern.slf4j.Slf4j;
import spring.filemanipulator.service.job.Job;
import spring.filemanipulator.utilities.long_computing.ComputingDelayer;

@Slf4j
public class TestBackgroundJob implements Job {

    public int delayInMillis = 0;

    public boolean shouldThrow = false;

    public volatile boolean startedExecuted = false;

    public volatile boolean stopExecuted = false;

    public TestBackgroundJob(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public TestBackgroundJob(boolean shouldAlwaysThrowException) {
        this.shouldThrow = shouldAlwaysThrowException;
    }


    @Override
    public Object start() {
        startedExecuted = !startedExecuted;

        if (shouldThrow) {
            throw new RuntimeException("This job is configured to always throw exception...");
        }

        new ComputingDelayer(delayInMillis).compute();

        return null;
    }

    @Override
    public void stop() {
        stopExecuted = !stopExecuted;
        log.info("Stop method executed.");
    }
}