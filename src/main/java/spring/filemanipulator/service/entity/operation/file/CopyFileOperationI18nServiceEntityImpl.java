package spring.filemanipulator.service.entity.operation.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import spring.filemanipulator.utilities.file_operation.CopyFileOperation;


@Service
public class CopyFileOperationI18nServiceEntityImpl extends AbstractFileOperationI18nNamedServiceEntity {

    private static final String UNIQUE_NAME_ID = "COPY";

    @Autowired
    public CopyFileOperationI18nServiceEntityImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource, new CopyFileOperation());
    }
}