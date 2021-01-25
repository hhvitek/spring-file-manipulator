package spring.filemanipulator.service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class WorkerManagerImpl implements WorkerManager {

    private static final int SCHEDULED_REPEATABLE_DELAY_IN_MILLIS = 5000;

    private int idCounter = 1;

    // synchronize keyword just one time method invocation synchronization, such as list::add, list::remove etc.
    // for iterators please use synchronize block...
    private final Map<Integer, WorkerManagerOneWorkerItem> workerIdToOneWorkerItem = Collections.synchronizedMap(new HashMap<>());

    private final ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    public WorkerManagerImpl(final ThreadPoolTaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;

        setTaskSchedulerPoolToNumberOfProcessors(taskScheduler);

        Runnable scheduledRepeatedTaskRunnable = this::scheduledRepeatedTask;
        taskScheduler.scheduleWithFixedDelay(scheduledRepeatedTaskRunnable, SCHEDULED_REPEATABLE_DELAY_IN_MILLIS);
    }

    private void setTaskSchedulerPoolToNumberOfProcessors(ThreadPoolTaskScheduler taskScheduler) {
        int cpus = Runtime.getRuntime().availableProcessors();
        log.info("Number of cpus: {}", cpus);
        taskScheduler.setPoolSize(cpus);
    }

    @Override
    public int schedule(Worker worker) {
        int workerId = generateUniqueId();

        CompletableFuture<Object> completableFuture = CompletableFuture
                .supplyAsync( worker::start, taskScheduler )
                .whenCompleteAsync((result, throwable) -> {
                    workerIdToOneWorkerItem.remove(workerId);

                    if (throwable != null) {
                        handleWorkerException(throwable, workerId);
                    } else {
                        handleWorkerResult(result, workerId);
                    }
                }, taskScheduler);

        // hmm, theoretically this code "can" be executed before the line above:
        //     workerIdToOneWorkerItem.remove(workerId);
        // soo let's just ignore it.
        WorkerManagerOneWorkerItem workerManagerOneWorkerItem = new WorkerManagerOneWorkerItem(workerId, worker, completableFuture);
        workerIdToOneWorkerItem.put(workerId, workerManagerOneWorkerItem);

        return workerId;
    }

    @Override
    public boolean contains(int workerId) {
        return workerIdToOneWorkerItem.containsKey(workerId);
    }

    private void handleWorkerException(Throwable throwable, int workerId) {
        log.warn("Job Exception thrown!!!!!!! workerId: {}", workerId);
    }

    private void handleWorkerResult(Object workerResult, int workerId) {
        log.warn("Job result: {}, Worker id: {}", workerResult, workerId);
    }

    private int generateUniqueId() {
        return idCounter++;
    }

    @Override
    public void stop(int workerId) throws WorkerNotFoundException {
        if (!contains(workerId)) {
            log.error("Worker id: {} not found.", workerId);
            throw new WorkerNotFoundException("Worker id: {} not found." + workerId);
        } else {
            performOneWorkerItemStop(workerIdToOneWorkerItem.get(workerId));
        }
    }

    private void performOneWorkerItemStop(WorkerManagerOneWorkerItem workerManagerOneWorkerItem) {
        Worker worker = workerManagerOneWorkerItem.getWorker();
        worker.stop();

        CompletableFuture<Object> future = workerManagerOneWorkerItem.getCompletableFuture();
        future.cancel(false); // bool does not have any effect
    }

    /**
     * Repeatable scheduled task, anything???
     */
    private void scheduledRepeatedTask() {
        //log.info("Scheduled Tasks repeatable thread executed!!!! Number of tasks: {}", workerIdToOneWorkerItem.size());

        synchronized (workerIdToOneWorkerItem) {
            for (Map.Entry<Integer, WorkerManagerOneWorkerItem> entry: workerIdToOneWorkerItem.entrySet()) {
                Integer workerId = entry.getKey();
                WorkerManagerOneWorkerItem item = entry.getValue();

                // do something
            }
        }
    }
}
