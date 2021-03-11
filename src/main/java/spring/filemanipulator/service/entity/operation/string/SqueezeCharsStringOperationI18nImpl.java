package spring.filemanipulator.service.entity.operation.string;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import spring.filemanipulator.utilities.string_filters.operations.squeeze.SqueezeCharacterFilter;

@Service
public class SqueezeCharsStringOperationI18nImpl extends AbstractStringOperationI18nNamedServiceEntity {

    public static final String UNIQUE_NAME_ID = "SQUEEZE";

    @Autowired
    protected SqueezeCharsStringOperationI18nImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource, new SqueezeCharacterFilter());
    }
}