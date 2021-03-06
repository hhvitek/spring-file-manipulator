package spring.filemanipulator.service.job.scheduler;

import lombok.extern.slf4j.Slf4j;
import spring.filemanipulator.service.job.Job;

@Slf4j
public class TestBackgroundJob implements Job {

    public int delayInMillis;

    public boolean shouldThrow;

    public volatile boolean startedExecuted;

    public volatile boolean stopExecuted;

    public TestBackgroundJob(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public TestBackgroundJob(boolean shouldThrow) {
        this.shouldThrow = shouldThrow;
    }


    @Override
    public Object start() {
        startedExecuted = !startedExecuted;

        if (shouldThrow) {
            throw new RuntimeException("This job is configured to always throw exception...");
        }

        try {
            Thread.sleep(delayInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void stop() {
        stopExecuted = !stopExecuted;
        log.info("Stop method executed.");
    }
}