package spring.filemanipulator.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import spring.filemanipulator.service.entity.task.TaskStatusServiceEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    private List<TaskStatusServiceEntity> taskStatusServiceEntities = new ArrayList<>();

    @Autowired
    public TaskStatusServiceImpl(final MessageSource messageSource) {
        for (TaskStatusType c: TaskStatusType.values()) {
            String taskStatusUniqueNameId = c.toString();
            TaskStatusServiceEntity entity = new TaskStatusServiceEntity(taskStatusUniqueNameId, messageSource);
            taskStatusServiceEntities.add(entity);
        }
    }

    @Override
    public Collection<TaskStatusServiceEntity> getAll() {
        return taskStatusServiceEntities;
    }

    @Override
    public boolean existsByUniqueName(String uniqueNameId) {
        return taskStatusServiceEntities.stream()
                .anyMatch(taskStatusServiceEntity -> taskStatusServiceEntity.getUniqueNameId().equals(uniqueNameId));
    }

    @Override
    public boolean isFinishedByName(String uniqueNameId) {
        if (!existsByUniqueName(uniqueNameId)) {
            return false;
        }

        return uniqueNameId.startsWith("FINISHED");
    }

    @Override
    public TaskStatusServiceEntity getInitStatus() {
        return taskStatusServiceEntities.get(0);
    }
}
