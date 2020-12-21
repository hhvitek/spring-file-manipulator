package spring.filemanipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.entity.task.TaskStatusServiceEntity;
import spring.filemanipulator.service.task.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController extends AbstractSearchableRestController<Integer, TaskEntity> {

    private final TaskService taskService;
    private final TaskStatusService taskStatusService;

    @Autowired
    protected TaskController(
            final TaskRepository repository,
            final TaskService taskService,
            final TaskStatusService taskStatusService) {
        super(repository);
        this.taskService = taskService;
        this.taskStatusService = taskStatusService;
    }

    @GetMapping("/{id:\\d+}/isFinished")
    public Map<String, Boolean> isFinished(@PathVariable Integer id) throws TaskNotFoundException {
        TaskEntity taskEntity = taskService.getTaskById(id);
        String taskStatusUniqueNameId = taskEntity.getTaskStatusUniqueNameId();
        return Map.of("finished", taskStatusService.isFinishedByName(taskStatusUniqueNameId));
    }

    @GetMapping("/task_statuses")
    public Collection<TaskStatusServiceEntity> getTaskStatuses() {
        return taskStatusService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createNewTask")
    public TaskEntity createNewTask(@RequestBody @Valid CreateTaskParametersDTO createTaskParametersDTO) throws InvalidCreateTaskParametersException {
        return taskService.createTask(createTaskParametersDTO);
    }
}
