package spring.filemanipulator.service.entity;

import org.springframework.context.NoSuchMessageException;

/**
 * Standard method supported across Service Entities
 *
 * Service Entity is an Entity that is not stored in database.
 */
public interface NamedServiceEntity {
    String getUniqueNameId() throws NoSuchMessageException;
    String getName() throws NoSuchMessageException;
    String getDescription() throws NoSuchMessageException;
}