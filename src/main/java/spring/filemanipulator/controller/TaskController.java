package spring.filemanipulator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.entity.NamedServiceEntity;
import spring.filemanipulator.service.task.InvalidCreateTaskParametersException;
import spring.filemanipulator.service.task.TaskNotFoundException;
import spring.filemanipulator.service.task.TaskService;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController extends AbstractSearchableRestController<TaskEntity, Integer> {

    private final TaskService taskService;

    @Autowired
    protected TaskController(
            final TaskRepository taskRepository,
            final TaskService taskService) {
        super(taskRepository);
        this.taskService = taskService;
    }

    @GetMapping("/{id:\\d+}/isFinished")
    public Map<String, Boolean> isFinished(@PathVariable Integer id) throws TaskNotFoundException {
        boolean isFinished = taskService.isFinished(id); // may throw

        return Map.of("finished", isFinished);
    }

    @GetMapping("/task_statuses")
    public Collection<NamedServiceEntity> getTaskStatuses() {
      //  return taskStatusI18nNamedServiceService.getAll();
        throw new UnsupportedOperationException();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createNewTask")
    public TaskEntity createNewTask(@RequestBody @Valid CreateTaskParametersDTO createTaskParametersDTO) throws InvalidCreateTaskParametersException {
        log.debug("The new createNewTask request received. DTO: {}", createTaskParametersDTO);
        return taskService.createAndSchedule(createTaskParametersDTO);
    }
}