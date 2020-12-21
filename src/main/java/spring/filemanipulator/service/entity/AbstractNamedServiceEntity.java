package spring.filemanipulator.service.entity;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;

public abstract class AbstractNamedServiceEntity implements NamedServiceEntity, Serializable {

    private static final String MESSAGES_PATH_NAME_SUFFIX = ".name";
    private static final String MESSAGES_PATH_DESCRIPTION_SUFFIX = ".description";

    private final MessageSource messageSource;

    private final String messagesPathToName;

    private final String messagesPathToDescription;

    private final String uniqueNameId;

    protected AbstractNamedServiceEntity(final String messagesPathPrefix, final String uniqueNameId, final MessageSource messageSource) {
        this.messageSource = messageSource;
        this.uniqueNameId = uniqueNameId;

        messagesPathToName = messagesPathPrefix + uniqueNameId + MESSAGES_PATH_NAME_SUFFIX;
        messagesPathToDescription = messagesPathPrefix + uniqueNameId + MESSAGES_PATH_DESCRIPTION_SUFFIX;
    }

    @Override
    public String getName() throws NoSuchMessageException {
        return messageSource.getMessage(messagesPathToName, null, LocaleContextHolder.getLocale());
    }

    @Override
    public String getDescription() throws NoSuchMessageException {
        return messageSource.getMessage(messagesPathToDescription, null, LocaleContextHolder.getLocale());
    }

    @Override
    public String getUniqueNameId() throws NoSuchMessageException {
        return uniqueNameId;
    }
}
