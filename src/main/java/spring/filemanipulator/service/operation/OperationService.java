package spring.filemanipulator.service.operation;

import spring.filemanipulator.service.entity.NamedServiceEntity;

import java.util.Collection;

/**<pre>
 * Generify common interfaces between Operations.
 * In this case FileOperationService and StringOperationService...
 *
 * T requirements:
 *    - implements NamedServiceEntity
 *    - it's semantically expected that T is going to have some execute() method to
 *    perform its Operation...
 * </pre>
 */
public interface OperationService<T extends NamedServiceEntity> {

    /**
     * Returns all OperationServiceEntities as NamedServiceEntity -- its not possible to execute them...
     */
    Collection<NamedServiceEntity> getAllNamedEntities();

    NamedServiceEntity getByIdNamedEntityIfNotFoundThrow(String uniqueNameId) throws OperationNotFoundException;

    /**
     * Returns NOT NamedServiceEntity, but the whole Abstracted implementation allowing to execute file operation.
     */
    T getByIdExecutableIfNotFoundThrow(String uniqueNameId) throws OperationNotFoundException;

    Collection<String> getAllUniqueNameIds();

    boolean existsByUniqueNameId(String uniqueNameId);
}