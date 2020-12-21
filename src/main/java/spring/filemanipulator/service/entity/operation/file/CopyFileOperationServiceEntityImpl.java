package spring.filemanipulator.service.entity.operation.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
public class CopyFileOperationServiceEntityImpl extends AbstractFileOperationNamedServiceEntity {

    private static final String UNIQUE_NAME_ID = "COPY";

    @Autowired
    protected CopyFileOperationServiceEntityImpl(final MessageSource messageSource) {
        super(UNIQUE_NAME_ID, messageSource);
    }

    @Override
    public void execute(Path inputFile, Path outputFile) throws FileOperationException {
        log.warn("--------------------COPY------------------");
    }


}
