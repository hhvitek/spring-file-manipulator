package spring.filemanipulator.utilities.file_locators;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class FileLocatorApacheCommons implements IFileLocator {

    private Path rootFolder;

    @Override
    public @NotNull List<Path> findUsingRegex(@NotNull Path rootFolder, @NotNull String fileRegex) {
        Collection<File> foundFiles = FileUtils.listFiles(
                rootFolder.toFile(),
                new WildcardFileFilter(fileRegex),
                TrueFileFilter.INSTANCE
        );
        return convertCollectionOfFilesIntoListOfPaths(foundFiles);
    }

    private List<Path> convertCollectionOfFilesIntoListOfPaths(Collection<File> collection) {
        return collection.stream()
                .map(File::toPath)
                .collect(Collectors.toList());
    }

    @Override
    public List<Path> listAllFiles(@NotNull Path rootFolder) throws FileLocatorException {
        Collection<File> foundFiles = FileUtils.listFiles(rootFolder.toFile(), null, true);
        return convertCollectionOfFilesIntoListOfPaths(foundFiles);
    }
}
