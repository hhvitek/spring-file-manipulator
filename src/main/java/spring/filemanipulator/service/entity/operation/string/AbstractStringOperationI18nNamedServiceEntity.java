package spring.filemanipulator.service.entity.operation.string;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import spring.filemanipulator.service.entity.AbstractI18nNamedServiceEntity;
import spring.filemanipulator.utilities.string_filters.operations.Operation;

@Slf4j
public abstract class AbstractStringOperationI18nNamedServiceEntity
        extends AbstractI18nNamedServiceEntity
        implements StringOperation {

    private static final String MESSAGES_PATH_PREFIX = "operations.string.";

    protected String replaceWhat;

    protected String replaceWith;

    protected final Operation operation;

    protected AbstractStringOperationI18nNamedServiceEntity(
            final String uniqueNameId,
            final MessageSource messageSource,
            final Operation operation

    ) {
        super(MESSAGES_PATH_PREFIX, uniqueNameId, messageSource);

        this.operation = operation;
    }

    @Override
    public String execute(String input) throws StringOperationException {
        log.debug("--EXECUTING {} STRING OPERATION with input {}--", getUniqueNameId(), input);

        String output =  performStringOperation(input);

        log.debug("--{} STRING OPERATION output {}--", getUniqueNameId(), output);

        return output;
    }

    protected String performStringOperation(String input) throws StringOperationException {
        return operation.filter(input);
    }

    @Override
    public void replaceWith(String with) {
        this.replaceWith = with;
    }

    @Override
    public void replaceWhat(String what) {
        this.replaceWhat = what;
    }
}