package spring.filemanipulator.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.repository.TaskRepository;

@Slf4j
@Service
public class TaskWorkerEventListenerImpl implements TaskWorkerEventListener {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskWorkerEventListenerImpl(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void handleTaskWorkerEvent(TaskWorkerEvent workerEvent) {
        log.info("Listener received event: {}", workerEvent);
    }
}
