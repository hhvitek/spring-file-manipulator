package spring.filemanipulator.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import spring.filemanipulator.entity.TaskEntity;

@Lazy
@Repository
public interface TaskRepository extends AbstractSearchableRepository<TaskEntity, Integer>, TaskRepositoryCustomMethods {
}