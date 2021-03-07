package spring.filemanipulator.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.task.manager.TaskJobEvent;

import java.util.concurrent.Executor;

/**<pre>
 * Single threaded background worker listener to TaskJob events
 *
 * We must ensure that all events are processed one-by-one as they received by this listener.
 * Otherwise the listener receives two events, but the one that arrived later
 * could potentially be resolved (and stored into DB) sooner than the one that arrived first.
 *
 * Therefore creating time-dependent problems.
 * <pre/>
 */
@Slf4j
@Component
public class TaskJobEventAsyncListener {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public TaskJobEventAsyncListener(final TaskRepository taskRepository, final TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

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

        TaskEntity entity = taskJobEvent.getTaskEntity();

        log.debug("----Updating database with: {}", entity);

        //taskRepository.save(entity);
    }
}