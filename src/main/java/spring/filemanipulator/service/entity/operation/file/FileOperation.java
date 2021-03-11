package spring.filemanipulator.service.entity.operation.file;

import java.nio.file.Path;

public interface FileOperation {
    void execute(Path sourceFolder, Path destinationFolder) throws FileOperationException;
}