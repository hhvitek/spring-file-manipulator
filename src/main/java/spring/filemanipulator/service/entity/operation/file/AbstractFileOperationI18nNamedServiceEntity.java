package spring.filemanipulator.service.entity.operation.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import spring.filemanipulator.service.entity.AbstractI18nNamedServiceEntity;
import spring.filemanipulator.utilities.file_operation.AbstractFileOperation;

import java.nio.file.Path;

/**
 * Common implementation for FileOperations.
 *
 * Extending NamedServiceEntity by execute() action
 */
@Slf4j
public abstract class AbstractFileOperationI18nNamedServiceEntity
        extends AbstractI18nNamedServiceEntity
        implements FileOperation {

    private static final String MESSAGES_PATH_PREFIX = "operations.file.";

    protected final AbstractFileOperation fileOperation;

    protected AbstractFileOperationI18nNamedServiceEntity(
            final String uniqueNameId,
            final MessageSource messageSource,
            final AbstractFileOperation fileOperation

    ) {
        super(MESSAGES_PATH_PREFIX, uniqueNameId, messageSource);

        this.fileOperation = fileOperation;
    }

    @Override
    public void execute(Path sourceFolder, Path destinationFolder) throws FileOperationException {
        log.debug("--EXECUTING {} OPERATION--", getUniqueNameId());
        performFileOperation(sourceFolder, destinationFolder);
    }

    protected void performFileOperation(Path sourceFolder, Path destinationFolder) throws FileOperationException {
        try {
            fileOperation.performFileOperation(sourceFolder, destinationFolder);
        } catch (spring.filemanipulator.utilities.file_operation.FileOperationException e) {
            throw new FileOperationException(e.toString());
        }
    }
}