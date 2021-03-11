package spring.filemanipulator.utilities.file_operation;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileOperationImpl implements FileOperation {
    @Override
    public void copy(Path what, Path to) throws FileOperationException {
        try {
            createParentDirectoryHierarchyIfNotAlreadyExists(to);
            Files.copy(what, to, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileOperationException(
                    FileOperationErrorsEnum.IO_OPERATION_ERROR,
                    e.toString()
            );
        }
    }

    private void createParentDirectoryHierarchyIfNotAlreadyExists(Path file) throws FileOperationException {
        Path parentFolder = file.getParent().toAbsolutePath();
        if (Files.notExists(parentFolder)) {
            createDirectoryOrThrowFileOperationException(parentFolder);
        }
    }

    private void createDirectoryOrThrowFileOperationException(Path folder) throws FileOperationException {
        try {
            Files.createDirectory(folder);
        } catch (IOException e) {
            throw new FileOperationException(FileOperationErrorsEnum.CANNOT_CREATE_FILE, "Failed to create folder: " + folder, e);
        }
    }

    @Override
    public void rename(Path what, String newName) throws FileOperationException {
        Path newPath = what.resolveSibling(newName);
        move(what, newPath);
    }

    @Override
    public void move(Path what, Path to) throws FileOperationException {
        try {
            FileUtils.moveFile(what.toFile(), to.toFile());
        } catch (IOException e) {
            throw new FileOperationException(FileOperationErrorsEnum.IO_OPERATION_ERROR, e.toString());
        }
    }

    @Override
    public void delete(Path what) throws FileOperationException {
        try (Stream<Path> walk = Files.walk(what)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new FileOperationException(FileOperationErrorsEnum.CANNOT_DELETE_FILE, "Failed to fully delete folder: " + what, e);
        }
    }
}