package spring.filemanipulator.repository;

import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.task.TaskNotFoundException;

public interface TaskRepositoryCustomMethods {
    TaskEntity findByIdIfNotFoundThrow(Integer taskId) throws TaskNotFoundException;
}