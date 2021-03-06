package spring.filemanipulator.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.task.manager.TaskEntityCreator;
import spring.filemanipulator.service.task.manager.TaskScheduler;
import spring.filemanipulator.service.task.status.TaskStatusI18nNamedServiceService;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTO;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskEntityCreator taskEntityCreator;

    private final TaskScheduler taskScheduler;

    private final TaskRepository taskRepository;

    private final TaskStatusI18nNamedServiceService taskStatusI18nNamedServiceService;


    @Autowired
    public TaskServiceImpl(
            final TaskEntityCreator taskEntityCreator,
            final TaskScheduler taskScheduler,
            final TaskRepository taskRepository,
            final TaskStatusI18nNamedServiceService taskStatusI18nNamedServiceService) {
        this.taskEntityCreator = taskEntityCreator;
        this.taskScheduler = taskScheduler;
        this.taskRepository = taskRepository;
        this.taskStatusI18nNamedServiceService = taskStatusI18nNamedServiceService;
    }

    @Override
    public TaskEntity createAndSchedule(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException {
        TaskEntity taskEntity = taskEntityCreator.createAndStore(dto);
        taskScheduler.scheduleAndStore(taskEntity);
        return taskEntity;
    }

    @Override
    public boolean isFinished(Integer taskId) throws TaskNotFoundException {
        TaskEntity taskEntity = taskRepository.findByIdIfNotFoundThrow(taskId);

        String taskStatusUniqueNameId = taskEntity.getTaskStatusUniqueNameId();

        return taskStatusI18nNamedServiceService.isNameConsideredFinished(taskStatusUniqueNameId);
    }

    @Override
    public void signalToStop(Integer taskId) throws TaskNotFoundException {
        taskScheduler.signalToStop(taskId);
    }


}