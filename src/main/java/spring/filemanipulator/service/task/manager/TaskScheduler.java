package spring.filemanipulator.service.task.manager;

import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.job.JobNotFoundException;
import spring.filemanipulator.service.task.TaskAlreadyFinishedException;
import spring.filemanipulator.service.task.TaskNotFoundException;
import spring.filemanipulator.service.task.TaskNotScheduledException;

/**
 * Schedules Task (represented by TaskEntity). It executes in its own background thread.
 */
public interface TaskScheduler {
    void scheduleAndStore(TaskEntity createdTaskEntity);

    /**
     * @param taskId
     * @throws TaskNotFoundException
     * @throws TaskNotScheduledException Simply put a Task has not yet been scheduled, therefore it cannot be stopped.
     * There is a Task record (TaskEntity) in DB, but no related Job record (JobEntity).
     * The JobEntity record is always created when a new Job is scheduled.
     *
     * @throws TaskAlreadyFinishedException If the task has already finished its execution, it, well, does not make sense
     * to stop...
     *
     * @throws JobNotFoundException This should never happen. Indicates DB inconsistency.
     * Mapping TaskEntity <-> JobEntity corrupted.
     */
    void signalToStopThrow(int taskId) throws
            TaskNotFoundException,
            TaskNotScheduledException,
            TaskAlreadyFinishedException,
            JobNotFoundException;
}