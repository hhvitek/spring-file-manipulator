package spring.filemanipulator.service.task.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.job.Job;
import spring.filemanipulator.service.job.JobAlreadyFinishedException;
import spring.filemanipulator.service.job.JobNotFoundException;
import spring.filemanipulator.service.job.JobService;
import spring.filemanipulator.service.task.TaskAlreadyFinishedException;
import spring.filemanipulator.service.task.TaskNotFoundException;
import spring.filemanipulator.service.task.TaskNotScheduledException;

@Service
public class TaskSchedulerImpl implements TaskScheduler {
    private final TaskJobFactory taskJobFactory;

    private final JobService jobService;

    private final TaskRepository taskRepository;


    @Autowired
    public TaskSchedulerImpl(TaskJobFactory taskJobFactory, JobService jobService, TaskRepository taskRepository) {
        this.taskJobFactory = taskJobFactory;
        this.jobService = jobService;
        this.taskRepository = taskRepository;
    }

    @Override
    public void scheduleAndStore(TaskEntity createdTaskEntity) {
        Job taskJob = taskJobFactory.createTaskJobByTaskEntity(createdTaskEntity);

        JobEntity jobEntity = jobService.createAndSchedule(taskJob);

        createdTaskEntity.setJobEntity(jobEntity);
        taskRepository.save(createdTaskEntity);
    }

    @Override
    public void signalToStopThrow(int taskId) throws TaskNotFoundException, TaskNotScheduledException, TaskAlreadyFinishedException, JobNotFoundException {
        TaskEntity taskEntity = taskRepository.findByIdIfNotFoundThrow(taskId);

        // on success should ensure that there is "related" Job
        int jobId = taskEntity.getJobIdIfNotScheduledThrow();

        // If this throws -> Database is corrupted. There should never be Job without Task in DB
        // TODO new DBinconsistency exception?
        signalJobToStopThrowOnNotFoundOrAlreadyFinished(jobId, taskId);
    }

    private void signalJobToStopThrowOnNotFoundOrAlreadyFinished(int jobId, int taskId) throws JobNotFoundException, TaskAlreadyFinishedException {
        try {
            jobService.signalToStopIfNotFoundThrow(jobId);
        } catch (JobAlreadyFinishedException ex) {
            throw new TaskAlreadyFinishedException(taskId);
        }
    }
}