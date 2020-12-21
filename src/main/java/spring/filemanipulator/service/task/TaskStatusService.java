package spring.filemanipulator.service.task;

import spring.filemanipulator.service.entity.task.TaskStatusServiceEntity;

import java.util.Collection;

public interface TaskStatusService {
    Collection<TaskStatusServiceEntity> getAll();
    boolean existsByUniqueName(String uniqueNameId);
    boolean isFinishedByName(String uniqueNameId);
    TaskStatusServiceEntity getInitStatus();
}
