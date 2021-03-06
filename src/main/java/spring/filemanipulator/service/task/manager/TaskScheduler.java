package spring.filemanipulator.service.task.manager;

import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.task.TaskNotFoundException;
import spring.filemanipulator.service.task.TaskNotScheduledException;

/**
 * Schedules Task (represented by TaskEntity). It executes in its own background thread.
 */
public interface TaskScheduler {
    void scheduleAndStore(TaskEntity createdTaskEntity);
    void signalToStop(int taskId) throws TaskNotFoundException, TaskNotScheduledException;
}