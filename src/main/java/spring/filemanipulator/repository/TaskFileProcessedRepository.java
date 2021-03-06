package spring.filemanipulator.repository;

import org.springframework.stereotype.Repository;
import spring.filemanipulator.entity.TaskFileProcessedEntity;

@Repository
public interface TaskFileProcessedRepository extends AbstractRepository<TaskFileProcessedEntity, Integer> {
}