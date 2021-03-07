package spring.filemanipulator.utilities.long_computing;

import lombok.extern.slf4j.Slf4j;

/**
 * Just one method compute(int delayInSeconds).
 *
 * Compute something for about delayInSeconds....
 */
@Slf4j
public class ComputingDelayer {

    private int delayInMillis;

    public ComputingDelayer() {
        this.delayInMillis = 1000;
    }

    public ComputingDelayer(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public void compute() {
        performComputeDelay(delayInMillis);
    }

    private void performComputeDelay(int delayInMillis) {
        try {
            log.debug("Computing Delayer executed for {} seconds....", delayInMillis / 1000);
            Thread.sleep(delayInMillis);
            log.debug("Computing Delayer finished....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void compute(int delayInMillis) {
        performComputeDelay(delayInMillis);
    }
}