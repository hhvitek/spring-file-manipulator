package spring.filemanipulator.service.entity.operation.string;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import spring.filemanipulator.utilities.string_filters.operations.filename.FileNameFilterOperation;

@Slf4j
@Service
public class NiceLookingFileNameStringOperationI18nImpl extends AbstractStringOperationI18nNamedServiceEntity {

    public static final String UNIQUE_NAME_ID = "NICE_LOOKING_FILENAME";

    @Autowired
    public NiceLookingFileNameStringOperationI18nImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource, new FileNameFilterOperation());
    }
}