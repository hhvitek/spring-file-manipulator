package spring.filemanipulator.service.task;

import spring.filemanipulator.entity.TaskEntity;

public interface TaskManager {

    TaskEntity schedule(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException;
    void stop(int taskId) throws TaskNotFoundException;
}
