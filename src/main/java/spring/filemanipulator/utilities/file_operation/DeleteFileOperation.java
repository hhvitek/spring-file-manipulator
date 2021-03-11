package spring.filemanipulator.utilities.file_operation;

import java.nio.file.Path;

public class DeleteFileOperation extends AbstractFileOperation {

    @Override
    public void performFileOperation(Path inputFile, Path outputFile) throws FileOperationException {
        fileOperation.delete(inputFile);
    }
}