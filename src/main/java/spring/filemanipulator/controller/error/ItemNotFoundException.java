package spring.filemanipulator.controller.error;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

/**
 * Resource not find, such as /tasks/33 if task with id=33 does not exist.
 */
public class ItemNotFoundException extends EntityNotFoundException {

    protected final Serializable id;

    public ItemNotFoundException(Serializable id) {
        super("Resource not found id = " + id);
        this.id = id;
    }
}
