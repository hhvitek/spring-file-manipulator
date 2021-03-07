package spring.filemanipulator.utilities.file_locators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

abstract class FileLocatorTest {

    IFileLocator locator;
    static Path rootFolder;

    @BeforeAll
    static void locateAndSetRootFolder() throws FileNotFoundException {
        try {
            File rootFolder = ResourceUtils.getFile("classpath:utilities/file_locators/root_folder_name/");
            FileLocatorTest.rootFolder = rootFolder.toPath();
        } catch (FileNotFoundException ex) {
            Assertions.fail("Cannot find requested resource utilities/file_locators/root_folder_name/", ex);
        }
    }

    abstract void listAllFilesTest();

    @Test
    void listAllRegularFilesTest()
    {
        int expectedFoundFiles = 8;

        List<Path> foundFiles = locator.listAllFiles(rootFolder);
        int actualFoundFiles = foundFiles.size();

        Assertions.assertEquals(expectedFoundFiles, actualFoundFiles);
    }

}