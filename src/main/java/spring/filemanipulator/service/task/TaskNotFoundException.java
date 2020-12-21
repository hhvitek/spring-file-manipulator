package spring.filemanipulator.service.task;

import spring.filemanipulator.controller.error.ItemNotFoundException;

import java.io.Serializable;


public class TaskNotFoundException extends ItemNotFoundException {

    public TaskNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getMessage() {
        return "Task not found id = " + id;
    }
}
