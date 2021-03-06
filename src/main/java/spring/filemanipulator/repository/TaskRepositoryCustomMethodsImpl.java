package spring.filemanipulator.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.task.TaskNotFoundException;

/**<pre>
 * Cannot use simple constructor injection.
 * There is a circular dependency between this Class and its "Parent" {@link TaskRepository}
 *
 * -> interface TaskRepository extends TaskRepositoryCustomMethods
 *  -> class TaskRepositoryCustomMethodsImpl implements TaskRepositoryCustomMethods
 * <- class TaskRepositoryCustomMethodsImpl requires TaskRepository instance...
 *
 * 1] Could use field or setter injection AND
 * Also this injection must be @Lazy (injected only when it is first needed) otherwise:
 *
 * > Error creating bean with name 'taskRepositoryImpl': Bean with name 'taskRepositoryImpl' has been injected into other beans [taskRepository]
 * >> in its raw version as part of a circular reference, but has eventually been wrapped.
 * >> This means that said other beans do not use the final version of the bean.
 * >> This is often the result of over-eager type matching - consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.
 *
 * 2] Just use @Lazy with constructor injection.
 * {@code
 *   @Autowired
 *   public TaskRepositoryCustomMethodsImpl(@Lazy TaskRepository taskRepository) {
 *       this.taskRepository = taskRepository;
 *   }
 * }
 * </pre>
 */
@Repository
public class TaskRepositoryCustomMethodsImpl implements TaskRepositoryCustomMethods {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskRepositoryCustomMethodsImpl(@Lazy TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskEntity findByIdIfNotFoundThrow(Integer taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }
}