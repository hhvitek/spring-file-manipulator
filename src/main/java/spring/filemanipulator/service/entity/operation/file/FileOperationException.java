package spring.filemanipulator.service.entity.operation.file;

public class FileOperationException extends RuntimeException {

    private final String message;

    public FileOperationException(String message) {
        super(message);

        this.message = message;
    }
}