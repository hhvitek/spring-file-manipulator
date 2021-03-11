package spring.filemanipulator.service.task.manager;

import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.job.Job;

public interface TaskJobFactory {
    Job createTaskJobByTaskEntity(TaskEntity taskEntity);
}