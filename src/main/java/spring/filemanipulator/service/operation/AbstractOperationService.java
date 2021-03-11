package spring.filemanipulator.service.operation;

import spring.filemanipulator.service.entity.NamedServiceEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**<pre>
 * Generic implementation of OperationService interface...
 *
 * It's expected that the actual implementation (T) is going to implement:
 * - NamedServiceEntity interface AND
 * - Allows to execute some Operation (action).
 * </pre>
 */
public abstract class AbstractOperationService<T extends NamedServiceEntity> implements OperationService<T> {

    protected final Collection<T> entities;

    protected AbstractOperationService(final List<T> entities) {
        this.entities = entities;
    }

    @Override
    public Collection<NamedServiceEntity> getAllNamedEntities() {
        return new ArrayList<>(entities);
    }

    @Override
    public NamedServiceEntity getByIdNamedEntityIfNotFoundThrow(String uniqueNameId) throws OperationNotFoundException {
        return getByIdExecutableIfNotFoundThrow(uniqueNameId);
    }

    @Override
    public T getByIdExecutableIfNotFoundThrow(String uniqueNameId) throws OperationNotFoundException {
        return entities.stream()
                .filter(entity -> entity.getUniqueNameId().equalsIgnoreCase(uniqueNameId))
                .findAny()
                .orElseThrow(() -> new OperationNotFoundException(uniqueNameId));
    }

    @Override
    public Collection<String> getAllUniqueNameIds() {
        return entities.stream()
                .map(NamedServiceEntity::getUniqueNameId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existsByUniqueNameId(String uniqueNameId) {
        return entities.stream()
                .anyMatch(entity -> entity.getUniqueNameId().equalsIgnoreCase(uniqueNameId));
    }
}