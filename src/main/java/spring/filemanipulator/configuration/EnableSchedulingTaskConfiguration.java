package spring.filemanipulator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

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