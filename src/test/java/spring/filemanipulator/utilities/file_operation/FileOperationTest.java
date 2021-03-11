package spring.filemanipulator.utilities.file_operation;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cannot run tests simultaneously...
 */
class FileOperationTest {

    protected final static Path RESOURCE_PATH = Path.of("./src/test/resources/utilities/file_operation");
    protected final static Path COPY_DIRECTORY = RESOURCE_PATH.resolve("copy");

    protected FileOperation fileOperation = new FileOperationImpl();

    @BeforeAll
    public static void initBeforeAll() {
        Assertions.assertTrue(Files.isDirectory(RESOURCE_PATH), "File not found: " + RESOURCE_PATH);
    }

    protected void recreateCopyFolder() throws IOException {
        if (Files.isDirectory(COPY_DIRECTORY)) {
            FileUtils.deleteDirectory(COPY_DIRECTORY.toFile());
        }

        Files.createDirectory(COPY_DIRECTORY);

        Path oneFile = COPY_DIRECTORY.resolve("existing-file.in");
        Files.createFile(oneFile);
    }

    @Test
    public void copyNonExistentFileTest() throws IOException {
        recreateCopyFolder();

        Path nonExistentFile = COPY_DIRECTORY.resolve("non-existent-file.in");
        Path destinationNonExistentFile = COPY_DIRECTORY.resolve("output-non-existent-file.out");
        Assertions.assertFalse(Files.exists(nonExistentFile));
        Assertions.assertFalse(Files.exists(destinationNonExistentFile));

        Assertions.assertThrows(
                FileOperationException.class,
                () -> fileOperation.copy(nonExistentFile, destinationNonExistentFile)
        );
    }

    @Test
    public void copyToNonExistentFolderTest() throws IOException {
        recreateCopyFolder();

        Path existingFile = COPY_DIRECTORY.resolve("existing-file.in");
        Path destinationNonExistentFile = COPY_DIRECTORY.resolve("OUTPUT/output-non-existent-file.out");

        Assertions.assertTrue(Files.isRegularFile(existingFile));
        Assertions.assertFalse(Files.exists(destinationNonExistentFile));

        Assertions.assertDoesNotThrow(
                () -> fileOperation.copy(existingFile, destinationNonExistentFile)
        );

        Assertions.assertTrue(Files.isRegularFile(existingFile));
        Assertions.assertTrue(Files.exists(destinationNonExistentFile));
    }

    @Test
    public void copyIfDestinationFileAlreadyExistTest() {

    }

}