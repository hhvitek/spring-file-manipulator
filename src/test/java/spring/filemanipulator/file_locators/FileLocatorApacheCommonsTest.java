package spring.filemanipulator.file_locators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.filemanipulator.utilities.file_locators.FileLocatorApacheCommons;

import java.nio.file.Path;
import java.util.List;

class FileLocatorApacheCommonsTest extends FileLocatorTest{

    @BeforeEach
    void init() {
        locator = new FileLocatorApacheCommons();
    }

    @Test
    @Override
    void listAllFilesTest() {
        int expectedFoundFiles = 8;

        List<Path> foundFiles = locator.findUsingRegex(rootFolder, "*");
        int actualFoundFiles = foundFiles.size();

        Assertions.assertEquals(expectedFoundFiles, actualFoundFiles);
    }
}
