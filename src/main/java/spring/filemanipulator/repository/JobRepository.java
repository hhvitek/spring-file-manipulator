package spring.filemanipulator.repository;

import org.springframework.stereotype.Repository;
import spring.filemanipulator.entity.JobEntity;


@Repository
public interface JobRepository extends AbstractRepository<JobEntity, Integer> {
}