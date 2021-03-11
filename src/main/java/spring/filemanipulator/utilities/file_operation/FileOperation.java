package spring.filemanipulator.utilities.file_operation;

import java.nio.file.Path;

public interface FileOperation {
    void copy(Path what, Path to) throws FileOperationException;
    void rename(Path what, String newName) throws FileOperationException;
    void move(Path what, Path to) throws FileOperationException;
    void delete(Path what) throws FileOperationException;
}