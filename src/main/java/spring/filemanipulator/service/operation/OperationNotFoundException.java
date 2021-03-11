package spring.filemanipulator.service.operation;

import spring.filemanipulator.controller.error.ItemNotFoundException;

public class OperationNotFoundException extends ItemNotFoundException {
    public OperationNotFoundException(String id) {
        super(id);
    }

    @Override
    public String getMessage() {
        return "The Operation (uniqueNameId=" + id + ") not found.";
    }

    @Override
    public String toString() {
        return "OperationNotFoundException: " + getMessage();
    }
}