package spring.filemanipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spring.filemanipulator.service.entity.NamedServiceEntity;
import spring.filemanipulator.service.task.status.TaskStatusI18nNamedServiceService;

import java.util.Collection;

@RestController
@RequestMapping("/api/tasks/taskStatus")
public class TaskStatusController {

    private final TaskStatusI18nNamedServiceService taskStatusService;

    @Autowired
    public TaskStatusController(final TaskStatusI18nNamedServiceService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<NamedServiceEntity> getAll() {
        return taskStatusService.getAll();
    }
}