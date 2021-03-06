package spring.filemanipulator.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * JpaSpecificationExecutor
 * Interface to allow execution of Specifications based on JPA criteria API.
 *
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaSpecificationExecutor.html">JpaSpecificationExecutor</a>
 */

@NoRepositoryBean // dont generate implementation
public interface AbstractSearchableRepository<Entity extends Serializable, ID extends Serializable> extends AbstractRepository<Entity, ID>, JpaSpecificationExecutor<Entity> {
}