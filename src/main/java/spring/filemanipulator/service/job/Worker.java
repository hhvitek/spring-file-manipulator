package spring.filemanipulator.service.job;

/**
 * Action, piece of code, executed sequentially using start() method. Returning "result" as Object.
 * Caller may invoke stop() method. This is just a signal. It actually does not stop the execution.
 * It is upon "the running" Worker instance to stop itself, whenever it feels so...
 */
public interface Worker {
    /**
     * Execute job synchronously. Returns something
     */
    Object start();

    /**
     * Signal to a running job to stop gracefully.
     * It is just "pretty please stop as soon as you want".
     * May actually stop a several "days" later...
     */
    void stop();
}
