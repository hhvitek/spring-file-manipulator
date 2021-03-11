package spring.filemanipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.filemanipulator.entity.TaskFileProcessedEntity;
import spring.filemanipulator.repository.TaskFileProcessedRepository;

@RestController
@RequestMapping("/api/tasks/taskFileProcessed")
public class TaskFileProcessedController extends AbstractRestController<TaskFileProcessedEntity, Integer> {

    @Autowired
    public TaskFileProcessedController(final TaskFileProcessedRepository taskFileProcessedRepository) {
        super(taskFileProcessedRepository);
    }

}