package spring.filemanipulator.utilities.file_locators;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public interface IFileLocator {

    /**
     * Searches rootFolder recursively. Finds all Files matched by @param fileRegex. Given
     * regex syntax adheres to implementation class
     * @param rootFolder Folder when the search is started.
     * @param fileRegex Regular expression defining file name format. The regex syntax is defined by implementation class
     * @return Found files as List of Paths.
     */
    @NotNull List<Path> findUsingRegex(@NotNull Path rootFolder, @NotNull String fileRegex) throws FileLocatorException;

    @NotNull List<Path> listAllFiles(@NotNull Path rootFolder) throws FileLocatorException;
}
