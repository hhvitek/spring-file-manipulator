package spring.filemanipulator.utilities.file_operation;

import java.nio.file.Path;

public class CopyFileOperation extends AbstractFileOperation {

    @Override
    public void performFileOperation(Path inputFile, Path outputFile) throws FileOperationException {
        fileOperation.copy(inputFile, outputFile);
    }
}