package spring.filemanipulator.service.job.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.filemanipulator.service.job.Job;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contains required structures to stop running jobs.
 * This requires jobId, Job itself to execute Job::stop method and also possibly Future<> java variable.
 *
 * This class should not throw any exception, if no requested job is found (such as delete(NonExistentJobId)).
 * If asked to operate on non-existent jobId, it does silently nothing.
 */
@Slf4j
@Service
public class RunningJobs {

    // synchronize keyword just one time method invocation synchronization, such as list::add, list::remove etc.
    // for iterators please use synchronized block...
    private final Map<Integer, RunningOneJobContainer> jobIdToScheduledOneJob = Collections.synchronizedMap(new HashMap<>());

    /**
     * Should be called when Future<> is not yet created to initialize data structures.
     */
    public void addNew(int id, Job job) {
        RunningOneJobContainer runningOneJobContainer = new RunningOneJobContainer(id, job);
        jobIdToScheduledOneJob.put(id, runningOneJobContainer);
    }

    /**
     * Should be called after addNew() has already been called AND immediately after Future<> has been created.
     */
    public void setFuture(int id, CompletableFuture<Object> completableFuture) {
        RunningOneJobContainer scheduledJob = jobIdToScheduledOneJob.get(id); // returns null if no id mapping
        if (scheduledJob != null) {
            scheduledJob.setCompletableFuture(completableFuture);
        } else {
            log.warn("Trying to setFuture variable, but no relevant running job found! Job id <{}>. " +
                    "This method must be called after addNew(ID) and before delete(ID)!", id);
        }
    }

    public void delete(int id) {
        jobIdToScheduledOneJob.remove(id); // if no id mapping returns null, no exception here
    }

    public boolean contains(int id) {
        return jobIdToScheduledOneJob.containsKey(id);
    }

    public void stop(int id)  {
        RunningOneJobContainer scheduledJob = jobIdToScheduledOneJob.get(id); // returns null if no id mapping

        if (scheduledJob != null && scheduledJob.getCompletableFuture() != null) {
            Job job = scheduledJob.getJob();
            job.stop();

            CompletableFuture<Object> future = scheduledJob.getCompletableFuture();
            future.cancel(false); // bool does not have any effect
        } else {
            log.warn("Trying to stop job id: <{}>. However either 1] no running job found. or 2] trying to stop to early...", id);
        }
    }
}