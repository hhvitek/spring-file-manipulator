package spring.filemanipulator.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Enables @Scheduled annotation.
 * And also one can configure, autowire and use spring ThreadPoolTaskScheduler object
 * which could simplify general task scheduling.
 *
 * @see <a href="https://www.baeldung.com/spring-task-scheduler">Spring-task-scheduler</a> at baeldung.com
 */
@Configuration
@EnableScheduling
public class EnableSchedulingTaskConfiguration {
}