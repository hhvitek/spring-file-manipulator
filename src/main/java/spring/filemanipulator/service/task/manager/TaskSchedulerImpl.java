package spring.filemanipulator.service.task.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.job.JobNotFoundException;
import spring.filemanipulator.service.job.JobService;
import spring.filemanipulator.service.task.TaskNotFoundException;
import spring.filemanipulator.service.task.TaskNotScheduledException;

@Service
public class TaskSchedulerImpl implements TaskScheduler {
    private final TaskJobFactoryImpl taskJobFactoryImpl;

    private final JobService jobService;

    private final TaskRepository taskRepository;


    @Autowired
    public TaskSchedulerImpl(TaskJobFactoryImpl taskJobFactoryImpl, JobService jobService, TaskRepository taskRepository) {
        this.taskJobFactoryImpl = taskJobFactoryImpl;
        this.jobService = jobService;
        this.taskRepository = taskRepository;
    }

    @Override
    public void scheduleAndStore(TaskEntity createdTaskEntity) {
        TaskJobImpl taskJobImpl = taskJobFactoryImpl.createTaskJobByTaskEntity(createdTaskEntity);

        JobEntity jobEntity = jobService.createAndSchedule(taskJobImpl);

        createdTaskEntity.setJobEntity(jobEntity);
        taskRepository.save(createdTaskEntity);
    }

    /**
     * @param taskId
     * @throws TaskNotFoundException
     * @throws TaskNotScheduledException Simply put a Task has not yet been scheduled, therefore it cannot be stopped.
     * There is a Task record (TaskEntity) in DB, but no related Job record (JobEntity).
     * The JobEntity record is always created when a new Job is scheduled.
     *
     * @throws JobNotFoundException This should never happen. Indicates DB inconsistency.
     * Mapping TaskEntity <-> JobEntity corrupted.
     */
    @Override
    public void signalToStop(int taskId) throws TaskNotFoundException, TaskNotScheduledException, JobNotFoundException {
        TaskEntity taskEntity = taskRepository.findByIdIfNotFoundThrow(taskId);

        // on success should ensure that there is "related" Job
        int jobId = taskEntity.getJobIdIfNotScheduledThrow();

        // If this throws -> Database is corrupted. There should never be Job without Task in DB
        jobService.signalToStopIfNotFoundThrow(jobId);
    }
}