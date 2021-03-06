package spring.filemanipulator.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;

/**
 * <pre>
 * JpaRepository extends PagingAndSortingRepository which in turn extends CrudRepository.
 *
 * Their main functions are:
 *
 * - CrudRepository mainly provides CRUD functions.
 * - PagingAndSortingRepository provides methods to do pagination and sorting records.
 * - JpaRepository provides some JPA-related methods such as flushing the persistence context and deleting records in a batch.
 * </pre>
 * @see <a href="https://www.baeldung.com/spring-data-repositories">baeldung comparation</a>
 */
@NoRepositoryBean // dont generate implementation
public interface AbstractRepository<Entity extends Serializable, ID extends Serializable> extends CrudRepository<Entity, ID> {

    /**
     * CrudRepository findAll returns Iterable.
     * This ensures additional compatibility.
     */
    @NotNull Collection<Entity> findAll();
}