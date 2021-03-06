package spring.filemanipulator.service.entity.operation.file;

import java.nio.file.Path;

public interface FileOperationServiceEntity {
    String getUniqueNameId();
    String getName();
    String getDescription();

    void execute(Path sourceFolder, Path destinationFolder) throws FileOperationException;
}