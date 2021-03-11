package spring.filemanipulator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.job.JobNotFoundException;
import spring.filemanipulator.service.task.*;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTO;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController extends AbstractSearchableRestController<TaskEntity, Integer> {

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    @Autowired
    protected TaskController(
            final TaskRepository taskRepository,
            final TaskService taskService) {
        super(taskRepository);
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @GetMapping("/{id:\\d+}/isFinished")
    public Map<String, Boolean> isFinished(@PathVariable Integer id) throws TaskNotFoundException {
        boolean isFinished = taskService.isFinished(id); // may throw

        return Map.of("finished", isFinished);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createNewTask")
    public TaskEntity createNewTask(@RequestBody @Valid CreateTaskParametersDTO createTaskParametersDTO) throws InvalidCreateTaskParametersException {
        log.debug("The new createNewTask request received. DTO: {}", createTaskParametersDTO);
        return taskService.createAndSchedule(createTaskParametersDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id:\\d+}/signalToStop")
    public TaskEntity signalToStop(@PathVariable Integer id) throws
            TaskNotFoundException, TaskAlreadyFinishedException, TaskNotScheduledException, JobNotFoundException {

        taskService.signalToStopIfNotFoundThrow(id);

        return taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );
    }
}