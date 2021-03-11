package spring.filemanipulator.service.job;

import spring.filemanipulator.controller.error.ItemNotFoundException;

public class JobNotFoundException extends ItemNotFoundException {

    private final int id;

    public JobNotFoundException(int jobId) {
        super(jobId);
        this.id = jobId;
    }

    @Override
    public String getMessage() {
        return "The Job (id=" + id + ") not found.";
    }

    @Override
    public String toString() {
        return getMessage();
    }
}