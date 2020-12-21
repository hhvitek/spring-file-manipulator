package spring.filemanipulator.service.entity.operation.string;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class SqueezeCharsStringOperationImpl extends AbstractStringOperationNamedServiceEntity {

    private static final String UNIQUE_NAME_ID = "SQUEEZE";

    @Autowired
    protected SqueezeCharsStringOperationImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource);
    }

    public String execute(String input) throws StringOperationException {
        return null;
    }
}
