package spring.filemanipulator.service.entity.operation.string;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import spring.filemanipulator.utilities.string_filters.operations.blacklist.BlacklistRegexFilter;

@Slf4j
@Service
public class ReplaceWhatRegexWithStringOperationI18nImpl extends AbstractStringOperationI18nNamedServiceEntity {

    public static final String UNIQUE_NAME_ID = "REPLACE_WHAT_WITH";

    @Autowired
    public ReplaceWhatRegexWithStringOperationI18nImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource, new BlacklistRegexFilter());
    }
}