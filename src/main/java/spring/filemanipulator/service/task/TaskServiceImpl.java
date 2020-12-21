package spring.filemanipulator.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;

@Slf4j
@Service("task-service")
@Primary
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskStatusService taskStatusService;

    private final TaskManager taskManager;


    @Autowired
    public TaskServiceImpl(final TaskRepository taskRepository,
                           final TaskStatusService taskStatusService,
                           final TaskManager taskManager) {
        this.taskRepository = taskRepository;
        this.taskStatusService = taskStatusService;
        this.taskManager = taskManager;
    }


    @Override
    public TaskEntity createTask(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException {

        TaskEntity taskEntity = taskManager.schedule(dto);

        return taskEntity;
    }

    @Override
    public TaskEntity createTaskUsingSettings() throws InvalidCreateTaskParametersException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public TaskEntity getTaskById(Integer taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    public void stopTaskById(Integer taskId) throws TaskNotFoundException {
        taskManager.stop(taskId);
    }
}
