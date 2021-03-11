package spring.filemanipulator.service.task;

import org.springframework.scheduling.config.Task;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.job.JobNotFoundException;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTO;

/**
 * Front face / interface for any controller / operation with Tasks.
 */
public interface TaskService {

    /**
     * Method receives user request parameters processed by Spring framework.
     * The spring framework should already validate those parameters syntactically.
     * This method also performs additional semantic validations, may throw exception.
     * It creates, stores and potentially execute (background thread) Task.
     *
     * @param dto Object created by Spring from user's http request body parameters...
     * @return Created Task represented by TaskEntity
     * @throws InvalidCreateTaskParametersException If for example passed parameters are invalid...
     */
    TaskEntity createAndSchedule(CreateTaskParametersDTO dto)
            throws InvalidCreateTaskParametersException;

    /**
     * Determine whether or not a task represented by taskId has finished.
     * Basically any task that has just been created or scheduled or it has been running for some time is considered NOT finished.
     *
     * Any task stopped by a user or a task that ended in error IS considered finished.
     * And then of course any task that finished gracefully...
     */
    boolean isFinished(Integer taskId) throws TaskNotFoundException;

    /**
     * Stop task if it is running.
     * Generally this "stop" should be approached with care. It may just flag a task to stop executing...
     *
     * @throws TaskNotFoundException If no task taskId exists.
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
    void signalToStopIfNotFoundThrow(Integer taskId) throws TaskNotFoundException;
}