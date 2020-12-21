package spring.filemanipulator.repository;

import org.springframework.stereotype.Repository;
import spring.filemanipulator.entity.TaskEntity;

@Repository
public interface TaskRepository extends AbstractSearchableRepository<Integer, TaskEntity> {
}
