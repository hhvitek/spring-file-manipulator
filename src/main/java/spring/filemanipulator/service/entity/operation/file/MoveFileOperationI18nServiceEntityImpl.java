package spring.filemanipulator.service.entity.operation.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import spring.filemanipulator.utilities.file_operation.MoveFileOperation;


@Service
public class MoveFileOperationI18nServiceEntityImpl extends AbstractFileOperationI18nNamedServiceEntity {

    private static final String UNIQUE_NAME_ID = "MOVE";

    @Autowired
    public MoveFileOperationI18nServiceEntityImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource, new MoveFileOperation());
    }
}