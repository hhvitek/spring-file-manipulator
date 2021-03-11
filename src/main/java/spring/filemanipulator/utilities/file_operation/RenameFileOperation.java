package spring.filemanipulator.utilities.file_operation;

import java.nio.file.Path;

public class RenameFileOperation extends AbstractFileOperation {

    @Override
    public void performFileOperation(Path inputFile, Path outputFile) throws FileOperationException {
        String outputFileName = outputFile.getFileName().toString();
        fileOperation.rename(inputFile, outputFileName);
    }
}