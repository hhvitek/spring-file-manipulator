package spring.filemanipulator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.filemanipulator.controller.error.ItemNotFoundException;
import spring.filemanipulator.repository.AbstractRepository;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Controller could subclass this class.
 * Contains common meaningful methods to get entities from database
 * using repository directly.
 */
public abstract class AbstractRestController<ID extends Serializable, E extends Serializable> {

    protected final AbstractRepository<ID, E> repository;

    protected AbstractRestController(AbstractRepository<ID, E> repository) {
        this.repository = repository;
    }

    @GetMapping
    public Collection<E> getAll() {
        return new ArrayList<>(repository.findAll());

    }

    @GetMapping("/{id:\\d+}")
    public E getOneById(@PathVariable ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public E createOne(@Valid @RequestBody E newOne) {
        return repository.save(newOne);
    }


}
