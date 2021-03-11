package spring.filemanipulator.service.task.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.TaskRepository;
import spring.filemanipulator.service.task.InvalidCreateTaskParametersException;
import spring.filemanipulator.service.task.status.TaskStatusI18nNamedServiceService;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTO;
import spring.filemanipulator.service.task.validator.CreateTaskParametersDTOValidator;


@Service
public class TaskEntityCreatorImpl implements TaskEntityCreator {
    private final TaskRepository taskRepository;

    private final TaskStatusI18nNamedServiceService taskStatusI18nNamedServiceService;

    private final CreateTaskParametersDTOValidator dtoValidator;

    @Autowired
    public TaskEntityCreatorImpl(final TaskRepository taskRepository,
                                 final TaskStatusI18nNamedServiceService taskStatusI18nNamedServiceService,
                                 final CreateTaskParametersDTOValidator dtoValidator
                           ) {
        this.taskRepository = taskRepository;
        this.taskStatusI18nNamedServiceService = taskStatusI18nNamedServiceService;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public TaskEntity create(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException {
        if (!this.dtoValidator.isValid(dto)) {
            throw new InvalidCreateTaskParametersException(this.dtoValidator.getLastErrors());
        }

        return TaskEntity.builder()
                .taskStatusUniqueNameId(taskStatusI18nNamedServiceService.getInitializationStatusUniqueId())
                .fileOperationUniqueNameId(dto.getFileOperation())
                .fileOperationInputFolder(dto.getSourceFolder())
                .fileOperationDestinationFolder(dto.getDestinationFolder())
                .stringOperationUniqueNameId(dto.getStringOperation())
                .stringOperationRegexWhat(dto.getStringOperationWhat())
                .stringOperationReplaceTo(dto.getStringOperationWith())
                .build();
    }

    @Override
    public TaskEntity createAndStore(CreateTaskParametersDTO dto) throws InvalidCreateTaskParametersException {
        TaskEntity taskEntity = create(dto);
        return taskRepository.save(taskEntity);
    }

}