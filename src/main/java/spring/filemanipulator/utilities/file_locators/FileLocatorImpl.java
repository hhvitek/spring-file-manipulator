package spring.filemanipulator.utilities.file_locators;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Expects FileMatcher regex format ^(glob|regex):(.+)$
 */
@Slf4j
@Service
public class FileLocatorImpl implements IFileLocator {

    private static final int MAX_RECURSIVE_DEPTH = 32;

    private static final PathMatcher PATH_MATCHER_ANY_FILE = FileSystems.getDefault().getPathMatcher("glob:**");

    private Path rootFolder;

    /**
     * Expects FileMatcher regex format ^(glob|regex):(.+)$
     */
    @Override
    public @NotNull List<Path> findUsingRegex(@NotNull Path rootFolder, @NotNull String syntaxAndPattern) throws FileLocatorException {
        this.rootFolder = rootFolder;

        if (doRootFolderExists()) {
            return findFilesUsingPathMatcher(syntaxAndPattern);
        } else {
            String errorMessage = String.format("Root folder <%s> does not exists!", rootFolder);
            log.error(errorMessage);
            throw new FileLocatorException(errorMessage);
        }
    }

    private boolean doRootFolderExists() {
        return rootFolder != null && Files.isDirectory(rootFolder);
    }

    private List<Path> findFilesUsingPathMatcher(@NotNull String syntaxAndPattern) throws FileLocatorException {
        PathMatcher pathMatcher = compileRegex(syntaxAndPattern);

        try (Stream<Path> matchedFilePath =
                findFilesFromRootFolderRecursivelyUsingPathMatcher(rootFolder, pathMatcher)
        ) {
            return matchedFilePath
                    .collect(Collectors.toList()
            );
        } catch (IOException e) {
            log.error("IOException encountered during file search.", e);
            throw new FileLocatorException(e);
        }
    }

    private PathMatcher compileRegex(@NotNull String syntaxAndPattern) throws FileLocatorException {
        try {
            return FileSystems.getDefault().getPathMatcher(syntaxAndPattern);
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException ex) {
            log.error("Syntax and Pattern is not valid! {}", syntaxAndPattern);
            throw new FileLocatorException(ex);
        }
    }

    private Stream<Path> findFilesFromRootFolderRecursivelyUsingPathMatcher(Path rootFolder, PathMatcher matcher)
        throws IOException
    {
        return Files.find(
                rootFolder,
                MAX_RECURSIVE_DEPTH,
                (path, basicFileAttribute) -> {
                    if (basicFileAttribute.isRegularFile()) {
                        Path fileName = path.getFileName();
                        return matcher.matches(fileName);
                    }
                    return false;
                }
        );
    }


    @Override
    public List<Path> listAllFiles(@NotNull Path rootFolder) throws FileLocatorException {
        this.rootFolder = rootFolder;

        try (Stream<Path> foundFiles =
                     findFilesFromRootFolderRecursivelyUsingPathMatcher(rootFolder, PATH_MATCHER_ANY_FILE);
        ) {
            return foundFiles.collect(Collectors.toList());
        }
        catch (IOException e) {
            log.error("IOException encountered during file search.", e);
            throw new FileLocatorException(e);
        }
    }
}
