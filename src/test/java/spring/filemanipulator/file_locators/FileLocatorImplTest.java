package spring.filemanipulator.file_locators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.filemanipulator.utilities.file_locators.FileLocatorImpl;

import java.nio.file.Path;
import java.util.List;

class FileLocatorImplTest extends FileLocatorTest {

    @BeforeEach
    void init() {
        locator = new FileLocatorImpl();
    }

    @Test
    void listAllFilesTest() {
        int expectedFoundFiles = 8;

        List<Path> foundFiles = locator.findUsingRegex(rootFolder, "glob:*");
        int actualFoundFiles = foundFiles.size();

        Assertions.assertEquals(expectedFoundFiles, actualFoundFiles);
    }
}
