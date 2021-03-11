package spring.filemanipulator.utilities.file_operation;

public class FileOperationException extends Exception {

    private static final long serialVersionUID = -2866997475370077145L;

    private final FileOperationErrorsEnum fileOperationErrorsEnum;

    public FileOperationException(FileOperationErrorsEnum error, String message) {
        super(message);
        fileOperationErrorsEnum = error;
    }

    public FileOperationException(FileOperationErrorsEnum error, String message, Throwable throwable) {
        super(message, throwable);
        fileOperationErrorsEnum = error;
    }

    @Override
    public String toString() {
        return fileOperationErrorsEnum + ": " + getLocalizedMessage();
    }
}