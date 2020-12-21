package spring.filemanipulator.service.entity.operation.string;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReplaceWhatRegexWithStringOperationImpl extends AbstractStringOperationNamedServiceEntity {

    private static final String UNIQUE_NAME_ID = "REPLACE_WHAT_WITH";

    @Autowired
    public ReplaceWhatRegexWithStringOperationImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource);
    }

    public String execute(String input) throws StringOperationException {
        return null;
    }
}
