package spring.filemanipulator.service.operation.string;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.service.entity.operation.string.AbstractStringOperationI18nNamedServiceEntity;
import spring.filemanipulator.service.operation.AbstractOperationService;

import java.util.List;

@Service
public class StringOperationServiceImpl
        extends AbstractOperationService<AbstractStringOperationI18nNamedServiceEntity>
        implements StringOperationService  {

    @Autowired
    public StringOperationServiceImpl(final List<AbstractStringOperationI18nNamedServiceEntity> entities) {
        super(entities);
    }
}