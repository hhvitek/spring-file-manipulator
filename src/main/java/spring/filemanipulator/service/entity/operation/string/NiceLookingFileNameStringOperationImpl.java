package spring.filemanipulator.service.entity.operation.string;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NiceLookingFileNameStringOperationImpl extends AbstractStringOperationNamedServiceEntity {

    private static final String UNIQUE_NAME_ID = "NICE_LOOKING_FILENAME";

    @Autowired
    public NiceLookingFileNameStringOperationImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource);
    }

    public String execute(String input) throws StringOperationException {
        log.warn("--------------------NICE FILENAMES------------------");
        return null;
    }


}
