package spring.filemanipulator.service.task.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.filemanipulator.service.entity.NamedServiceEntity;
import spring.filemanipulator.service.entity.task.TaskStatusI18nNameServiceEntity;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


@SpringBootTest(classes = TaskStatusI18nNamedServiceService.class)
class TaskStatusI18nNamedServiceServiceTest {

    private final TaskStatusI18nNamedServiceService taskStatusI18nNamedServiceService;

    @Autowired
    TaskStatusI18nNamedServiceServiceTest(TaskStatusI18nNamedServiceService taskStatusI18nNamedServiceService) {
        this.taskStatusI18nNamedServiceService = taskStatusI18nNamedServiceService;
    }

    @Test
    public void getAllDoesNotFailOnMessagesErrorTest() {
        assertThatNoException().isThrownBy(
                () -> taskStatusI18nNamedServiceService.getAll()
        );

    }

    @Test
    public void noNewOrRemovedStatusesTest() {
        Collection<NamedServiceEntity> entities = taskStatusI18nNamedServiceService.getAll();
        assertThat(entities).hasSize(7);
    }
}