package spring.filemanipulator.utilities.file_operation;

import java.nio.file.Path;

public abstract class AbstractFileOperation {

    protected FileOperation fileOperation = new FileOperationImpl();

    public abstract void performFileOperation(Path inputFile, Path outputFile) throws FileOperationException;
}