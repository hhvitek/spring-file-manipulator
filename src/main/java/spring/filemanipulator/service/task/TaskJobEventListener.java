package spring.filemanipulator.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;

@Slf4j
@Service
public class TaskJobEventListener {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public TaskJobEventListener(final TaskRepository taskRepository, final TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @Async
    @EventListener
    public void handleTaskJobEvent(TaskJobEventImpl taskEvent) {
        log.info("Task Listener received event: {}", taskEvent);

        TaskEntity entity = (TaskEntity) taskEvent.firstValue();

        log.info("Updating database with: {}", entity);

        //taskRepository.save(entity);
    }
}