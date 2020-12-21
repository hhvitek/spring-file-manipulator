package spring.filemanipulator.utilities.file_locators;

public class FileLocatorException extends RuntimeException {

    private static final long serialVersionUID = -846405353726804854L;

    public FileLocatorException(String message) {
        super(message);
    }

    public FileLocatorException(Throwable throwable) {
        super(throwable);
    }
}
