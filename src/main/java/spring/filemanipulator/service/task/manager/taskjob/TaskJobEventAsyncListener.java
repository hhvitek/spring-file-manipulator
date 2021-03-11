package spring.filemanipulator.service.task.manager.taskjob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.entity.TaskFileProcessedEntity;
import spring.filemanipulator.repository.TaskFileProcessedRepository;
import spring.filemanipulator.repository.TaskRepository;

import java.util.concurrent.Executor;

/**<pre>
 * Single threaded background worker listener to TaskJob events
 *
 * We must ensure that all events are processed one-by-one as they are being received by the listener.
 * Otherwise the listener receives two events, but the one that arrived later
 * could potentially be resolved (and stored into DB) sooner than the one that arrived first.
 *
 * Therefore creating time-dependent problems.
 *
 * Also since we are using Sqlite DB it is much better to serialize sql queries whenever possible...
 * <pre/>
 */
@Slf4j
@Component
public class TaskJobEventAsyncListener {

    private final TaskRepository taskRepository;
    private final TaskFileProcessedRepository taskFileProcessedRepository;

    @Autowired
    public TaskJobEventAsyncListener(
            final TaskRepository taskRepository,
            final TaskFileProcessedRepository taskFileProcessedRepository) {
        this.taskRepository = taskRepository;
        this.taskFileProcessedRepository = taskFileProcessedRepository;
    }

    /**
     * Let's explain ThreadPoolTaskExecutor
     * corePoolSize - Default 1. If FULL then it prefers to queue new workers.
     * maxPoolSize
     * queueCapacity - Default INFINITY. If FULL And corePoolSize FULL than new workers up to maxPoolSize.
     *
     * This means that maxPoolSize is mostly irrelevant if configured alone!!!
     */
    @Bean(name = "taskListener")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1); // queuing all event handling
        executor.setThreadNamePrefix("taskListener-");
        executor.initialize();
        log.debug("taskListener: corePoolSize-{}, maxPoolSize-{}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize());
        return executor;
    }

    @Async("taskListener")
    @EventListener // annotation driver event listener
    public void handleTaskJobEvent(TaskJobEvent taskJobEvent) {
        log.debug("--Task Listener received event: {}", taskJobEvent.toStringShort());
        saveTaskEntity(taskJobEvent);

        saveTaskFileProcessedEntityIfRelevantEvent(taskJobEvent);
    }

    private void saveTaskEntity(TaskJobEvent event) {
        TaskEntity entity = event.getTaskEntity();
        log.debug("----Updating task entity with: {}", entity);

        taskRepository.save(entity);
    }

    private void saveTaskFileProcessedEntityIfRelevantEvent(TaskJobEvent event) {
        if (event.isFileProcessedEvent()) {
            TaskFileProcessedEntity entity = event.getTaskFileProcessedEntity();
            log.debug("----Updating task file processed: {}", entity);

            taskFileProcessedRepository.save(entity);
        }
    }
}