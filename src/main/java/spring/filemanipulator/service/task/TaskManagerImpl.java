package spring.filemanipulator.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.entity.task.TaskStatusServiceEntity;
import spring.filemanipulator.service.job.Worker;
import spring.filemanipulator.service.job.WorkerManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TaskManagerImpl implements TaskManager {
    private final TaskWorkerFactory taskWorkerFactory;

    private final TaskRepository taskRepository;

    private final TaskStatusService taskStatusService;

    private final CreateTaskParametersDTOValidator createTaskParametersDTOValidator;

    private final WorkerManager workerManager;

    private final Map<Integer, Integer> taskIdToWorkerIdMap = new HashMap<>();

    @Autowired
    public TaskManagerImpl(final TaskWorkerFactory taskWorkerFactory,
                           final TaskRepository taskRepository,
                           final TaskStatusService taskStatusService,
                           final CreateTaskParametersDTOValidator createTaskParametersDTOValidator,
                           final WorkerManager workerManager) {
        this.taskWorkerFactory = taskWorkerFactory;
        this.taskRepository = taskRepository;
        this.taskStatusService = taskStatusService;
        this.createTaskParametersDTOValidator = createTaskParametersDTOValidator;
        this.workerManager = workerManager;
    }


    @Override
    public TaskEntity schedule(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException {
        if (!createTaskParametersDTOValidator.isValid(dto)) {
            throw new InvalidCreateTaskParametersException(createTaskParametersDTOValidator.getLastErrors());
        }

        TaskEntity taskEntity = createTaskEntityFromDTO(dto);
        taskRepository.save(taskEntity); // generate id

        Worker worker = taskWorkerFactory.getTaskWorker(taskEntity);
        int scheduledJobId = workerManager.schedule(worker);
        taskIdToWorkerIdMap.put(taskEntity.getId(), scheduledJobId);

        return taskEntity;
    }

    /**
     * No validation here...
     */
    private TaskEntity createTaskEntityFromDTO(CreateTaskParametersDTO dto) {
        return TaskEntity.builder()
                .taskStatusUniqueNameId(getUniqueNameIdOfInitTaskStatus())
                .fileOperationUniqueNameId(dto.getFileOperationUniqueNameId())
                .fileOperationInputFolder(dto.getSourceFolder())
                .fileOperationDestinationFolder(dto.getDestinationFolder())
                .stringOperationUniqueNameId("HELLO_MY_LOVE")
                .build();
    }

    private String getUniqueNameIdOfInitTaskStatus() {
        TaskStatusServiceEntity serviceEntity = taskStatusService.getInitStatus();
        return serviceEntity.getUniqueNameId();
    }


    @Override
    public void stop(int taskId) throws TaskNotFoundException {
        if (!contains(taskId)) {
            throw new TaskNotFoundException("Task not scheduled id: " + taskId);
        }

        int workerId = taskIdToWorkerIdMap.get(taskId);
        workerManager.stop(workerId);
    }

    private boolean contains(int taskId) {
        return taskIdToWorkerIdMap.containsKey(taskId);
    }

}
