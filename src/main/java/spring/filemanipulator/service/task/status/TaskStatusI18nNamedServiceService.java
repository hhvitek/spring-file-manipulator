package spring.filemanipulator.service.task.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import spring.filemanipulator.service.entity.NamedServiceEntity;
import spring.filemanipulator.service.entity.task.TaskStatusI18nNameServiceEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO this is not implemented i18n - works just with uniqe name ids...
/**
 * API to work with TaskStatus UniqueNameId strings
 * Does not check actually Tasks statuses!! Use TaskService for that purpose...
 *
 * Use in internalization environment. See {@link TaskStatusEnum} for actual unique name ids types...
 *
 * Those ids are translated to and from internationalized strings...
 */
@Service
public class TaskStatusI18nNamedServiceService {

    private final List<NamedServiceEntity> taskStatusI18nNameServiceEntities = new ArrayList<>();

    @Autowired
    public TaskStatusI18nNamedServiceService(final MessageSource messageSource) {
        for (TaskStatusEnum c: TaskStatusEnum.values()) {
            String taskStatusUniqueNameId = c.toString();
            TaskStatusI18nNameServiceEntity entity = new TaskStatusI18nNameServiceEntity(taskStatusUniqueNameId, messageSource);
            taskStatusI18nNameServiceEntities.add(entity);
        }
    }

    public Collection<NamedServiceEntity> getAll() {
        return taskStatusI18nNameServiceEntities;
    }

    /**
     * To check if the parameter string represents existing TaskStatus name or not.
     */
    public boolean existsByUniqueName(String uniqueNameId) {
        return taskStatusI18nNameServiceEntities.stream()
                .anyMatch(taskStatusI18nNameServiceEntity -> taskStatusI18nNameServiceEntity.getUniqueNameId().equals(uniqueNameId));
    }

    /**<pre>{@code
     * Task is considered finish in the following basic cases (may be more):
     * - Stopped by user,
     * - Finished with or without errors.
     * - Other errors.
     *
     * Naming conventions should be decided by implementation.
     * }</pre>
     */
    public boolean isNameConsideredFinished(String uniqueNameId) {
        if (!existsByUniqueName(uniqueNameId)) {
            return false;
        }

        return uniqueNameId.startsWith("FINISHED");
    }

    /**
     * The task status name that a new task should be initialized with.
     */
    public String getInitializationStatusUniqueId() {
        NamedServiceEntity entity = taskStatusI18nNameServiceEntities.get(0);
        return entity.getUniqueNameId();
    }
}