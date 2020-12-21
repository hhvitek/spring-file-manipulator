package spring.filemanipulator.service.task;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface TaskWorkerEventListener {
    @Async
    @EventListener
    void handleTaskWorkerEvent(TaskWorkerEvent workerEvent);
}
