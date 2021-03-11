package spring.filemanipulator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.filemanipulator.controller.error.ItemNotFoundException;
import spring.filemanipulator.repository.AbstractRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Any controller may extends this class.
 * Contains common meaningful methods to get entities from database
 * This abstract class is using repository directly.
 */
public abstract class AbstractRestController<Entity extends Serializable, ID extends Serializable> {

    protected final AbstractRepository<Entity, ID> repository;

    protected AbstractRestController(AbstractRepository<Entity, ID> repository) {
        this.repository = repository;
    }

    @GetMapping
    public Collection<Entity> getAll() {
        return new ArrayList<>(repository.findAll());

    }

    @GetMapping("/{id:\\d+}")
    public Entity findByIdIfNotFoundThrow(@PathVariable ID id) throws ItemNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }
}