package spring.filemanipulator.service.entity.operation.string;

public interface StringOperation {
    String execute(String input) throws StringOperationException;

    void replaceWhat(String what);
    void replaceWith(String with);
}