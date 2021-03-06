package spring.filemanipulator.service.task.manager;

import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.task.InvalidCreateTaskParametersException;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTO;

/**
 * Use to create Task's data representation called TaskEntity from User's passed parameters.
 * User's CreateTaskParametersDTO --> App's TaskEntity
 */
public interface TaskEntityCreator {
    TaskEntity create(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException;
    TaskEntity createAndStore(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException;
}