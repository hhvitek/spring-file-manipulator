package spring.filemanipulator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * In internalization environment use MessageSource in conjunction
 * with @Valid annotation.
 *
 * @NotBlank(message = "{task.create.errors.sourceFolder.missing}")
 * private String sourceFolder;
 */
@Configuration
public class EnableI18nCustomValidationMessage {

    @Autowired
    @Bean
    public LocalValidatorFactoryBean getValidator(final MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}