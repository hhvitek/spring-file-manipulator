package spring.filemanipulator.service.entity;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.util.Locale;

/**<pre>
 * Idea is to support internalization in the NamedServiceEntities
 *
 * uniqueNameId, name, description
 *
 * Path in the messages.properties file:
 *      messagesPathPrefix.uniqueNameId.suffix
 *
 *      operations.file.COPY.name=COPY
 *      operations.file.COPY.description=Description message
 * </pre>
 */
public abstract class AbstractI18nNamedServiceEntity implements NamedServiceEntity, Serializable {

    private static final String MESSAGES_PATH_NAME_SUFFIX = ".name";
    private static final String MESSAGES_PATH_DESCRIPTION_SUFFIX = ".description";

    private final MessageSource messageSource;

    private final String messagesPathToName;

    private final String messagesPathToDescription;

    private final String uniqueNameId;

    protected AbstractI18nNamedServiceEntity(final String messagesPathPrefix, final String uniqueNameId, final MessageSource messageSource) {
        this.messageSource = messageSource;
        this.uniqueNameId = uniqueNameId;

        messagesPathToName = messagesPathPrefix + uniqueNameId + MESSAGES_PATH_NAME_SUFFIX;
        messagesPathToDescription = messagesPathPrefix + uniqueNameId + MESSAGES_PATH_DESCRIPTION_SUFFIX;
    }

    @Override
    public String getName() throws NoSuchMessageException {
        return messageSource.getMessage(messagesPathToName, null, LocaleContextHolder.getLocale());
        // return messageSource.getMessage(messagesPathToName, null, "default message here", LocaleContextHolder.getLocale());
        // does not throw instead return default message which can be null
    }

    @Override
    public String getDescription() throws NoSuchMessageException {
        return messageSource.getMessage(messagesPathToDescription, null, LocaleContextHolder.getLocale());
    }

    @Override
    public String getUniqueNameId() throws NoSuchMessageException {
        return uniqueNameId;
    }

    public String getName(Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(messagesPathToName, null, locale);
    }

    public String getDescription(Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(messagesPathToDescription, null, locale);
    }
}