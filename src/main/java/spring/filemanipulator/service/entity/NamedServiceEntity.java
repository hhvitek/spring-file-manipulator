package spring.filemanipulator.service.entity;

import org.springframework.context.NoSuchMessageException;

public interface NamedServiceEntity {
    String getUniqueNameId() throws NoSuchMessageException;
    String getName() throws NoSuchMessageException;
    String getDescription() throws NoSuchMessageException;
}
