package spring.filemanipulator.service.job;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface EventAwareWorkerListener {
    @Async
    @EventListener
    void handleWorkerEvent(WorkerEvent workerEvent);
}
