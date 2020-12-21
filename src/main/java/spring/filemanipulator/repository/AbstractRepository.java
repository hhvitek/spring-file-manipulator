package spring.filemanipulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AbstractRepository<ID extends Serializable, E extends Serializable> extends JpaRepository<E, ID> {
}
