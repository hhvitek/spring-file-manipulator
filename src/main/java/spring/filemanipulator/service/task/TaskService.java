package spring.filemanipulator.service.task;

import spring.filemanipulator.entity.TaskEntity;

public interface TaskService {

    TaskEntity createTask(CreateTaskParametersDTO createTaskParametersDTO)
            throws InvalidCreateTaskParametersException;

    /**
     * To determine required parameters uses data stored in SettingsRepository.
     * InvalidParameterException is thrown If any data is missing in repository and/or invalid
     *
     * @throws InvalidCreateTaskParametersException
     */
    TaskEntity createTaskUsingSettings() throws InvalidCreateTaskParametersException;

    TaskEntity getTaskById(Integer taskId) throws TaskNotFoundException;

    /**
     * Stop task if it is running.
     * @param taskId task id
     * @throws TaskNotFoundException If no task taskId exists.
     */
    void stopTaskById(Integer taskId) throws TaskNotFoundException;
}
