package spring.filemanipulator.service.task.manager.taskjob;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.entity.operation.file.FileOperation;
import spring.filemanipulator.service.entity.operation.file.FileOperationException;
import spring.filemanipulator.service.entity.operation.string.StringOperation;
import spring.filemanipulator.service.entity.operation.string.StringOperationException;
import spring.filemanipulator.service.operation.OperationNotFoundException;
import spring.filemanipulator.service.operation.file.FileOperationService;
import spring.filemanipulator.service.operation.string.StringOperationService;
import spring.filemanipulator.service.task.manager.TaskJobProcessingException;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**<pre>
 * Created by TaskJobFactory that instantiates all TaskJobs implementations.
 * Factory uses Springs ObjectProvider to instantiate this class.
 *      => It ensures that every Thread has its own instance...!!!
 *
 * Caller must invoke initialize* method first!!!
 *</pre>
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // always generate a new instance when requested
                                                        // let each task having its own instance
public class TaskJobFileProcessor {
    private final FileOperationService fileOperationService;
    private final StringOperationService stringOperationService;

    private Path destinationFolder;

    private FileOperation fileOperation;

    private StringOperation stringOperation;

    @Autowired
    public TaskJobFileProcessor(FileOperationService fileOperationService, StringOperationService stringOperationService) {
        this.fileOperationService = fileOperationService;
        this.stringOperationService = stringOperationService;
    }

    /**
     * @param taskEntity
     * @throws OperationNotFoundException
     * @throws InvalidPathException
     */
    public void initializeFromTaskEntity(TaskEntity taskEntity) throws OperationNotFoundException, InvalidPathException {
        this.destinationFolder = Path.of(taskEntity.getFileOperationDestinationFolder());
        this.fileOperation = fileOperationService.getByIdExecutableIfNotFoundThrow(taskEntity.getFileOperationUniqueNameId());

        this.stringOperation = stringOperationService.getByIdExecutableIfNotFoundThrow(taskEntity.getStringOperationUniqueNameId());
        String replaceWith = taskEntity.getStringOperationReplaceTo();
        String replaceWhat = taskEntity.getStringOperationRegexWhat();
        stringOperation.replaceWhat(replaceWhat);
        stringOperation.replaceWith(replaceWith);
    }

    public void performAllOperations(Path sourceFile) throws TaskJobProcessingException, InvalidPathException, StringOperationException, FileOperationException {
        Path destinationFile = performStringOperation(sourceFile);
        performFileOperation(sourceFile, destinationFile);
    }

    public Path performStringOperation(Path sourceFile) throws TaskJobProcessingException, StringOperationException {
        throwExceptionIfHasNotBeenInitialized();
        return executeStringOperation(sourceFile);

    }

    private void throwExceptionIfHasNotBeenInitialized() throws TaskJobProcessingException, StringOperationException {
        if (!hasBeenProperlyInitialized()) {
            log.error("Must be initialized first by calling initialize() method...");
            throw new TaskJobProcessingException("Internal error: Must be initialized first by calling initialize() method...");
        }
    }

    private boolean hasBeenProperlyInitialized() {
        return destinationFolder != null
                && fileOperation != null
                && stringOperation != null;
    }

    private Path executeStringOperation(Path sourceFile) throws StringOperationException {
        try {
            log.debug("--Performing stringOperation of file: {}", sourceFile);
            String oldFileName = sourceFile.getFileName().toString();
            String oldFileBaseName = FilenameUtils.getBaseName(oldFileName);
            String oldFileExtension = FilenameUtils.getExtension(oldFileName);

            String newFileBaseName = stringOperation.execute(oldFileBaseName);
            String newFileName = appendExtensionToFileBaseName(newFileBaseName, oldFileExtension);

            Path destinationFile = destinationFolder.resolve(newFileName);
            log.debug("----New fileName after stringOperation: {}", destinationFile);
            return destinationFile;
        } catch (IllegalArgumentException e) {
            throw new StringOperationException(e);
        }
    }

    private String appendExtensionToFileBaseName(@NotNull String baseName, @Nullable String extension) {
        if (extension != null && !extension.isEmpty()) {
            baseName = baseName + "." + extension;
        }
        return baseName;
    }

    public void performFileOperation(Path sourceFile, Path destinationFile) throws FileOperationException {
        throwExceptionIfHasNotBeenInitialized();
        executeFileOperation(sourceFile, destinationFile);
    }

    private void executeFileOperation(Path sourceFile, Path destinationFile) throws FileOperationException {
        log.debug("--Performing fileOperation. source: {}. destination: {}.", sourceFile, destinationFile);
        fileOperation.execute(sourceFile, destinationFile);
        log.debug("--Performed ok.");
    }


}