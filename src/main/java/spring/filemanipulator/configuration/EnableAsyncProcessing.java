package spring.filemanipulator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Enable asynchronous (@Async) execution.
 * Required to run background tasks in the application.
 */
@Configuration
@EnableAsync
public class EnableAsyncProcessing {
}