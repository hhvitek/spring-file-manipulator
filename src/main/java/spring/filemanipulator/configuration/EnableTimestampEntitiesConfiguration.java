package spring.filemanipulator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * <pre>{@code
 * This is necessary annotation to allow spring data jpa to track createdDate and lastModifiedDate in database.
 * (one of many capability of this annotation)
 *
 * @MappedSuperclass
 * @EntityListeners(AuditingEntityListener.class)
 * public abstract class AbstractTimestampEntity implements Serializable {
 *
 *     @CreatedDate
 *     @Column(nullable = false, updatable = false)
 *     protected LocalDateTime createdDate;
 *
 *     @LastModifiedDate
 *     @Column(nullable = false)
 *     protected LocalDateTime lastModifiedDate;
 * }
 * }<pre/>
 */
@Configuration
@EnableJpaAuditing
public class EnableTimestampEntitiesConfiguration {
}