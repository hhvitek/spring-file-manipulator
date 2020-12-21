package spring.filemanipulator.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AbstractSearchableRepository<ID extends Serializable, E extends Serializable> extends AbstractRepository<ID, E>, JpaSpecificationExecutor<E> {
}
