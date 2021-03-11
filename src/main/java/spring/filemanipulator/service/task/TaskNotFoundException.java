package spring.filemanipulator.service.task;

import spring.filemanipulator.controller.error.ItemNotFoundException;


public class TaskNotFoundException extends ItemNotFoundException {

    private final int id;

    public TaskNotFoundException(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "The Task (id=" + id + ") not found.";
    }

    @Override
    public String toString() {
        return "TaskNotFoundException: " + getMessage();
    }
}