package spring.filemanipulator.service.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.filemanipulator.service.entity.task.TaskStatusServiceEntity;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


@SpringBootTest(classes = TaskStatusServiceImpl.class)
class TaskStatusServiceImplTest {

    private final TaskStatusService taskStatusService;

    @Autowired
    TaskStatusServiceImplTest(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @Test
    public void getAllDoesNotFailOnMessagesErrorTest() {
        assertThatNoException().isThrownBy(
                () -> taskStatusService.getAll()
        );

    }

    @Test
    public void noNewOrRemovedStatusesTest() {
        Collection<TaskStatusServiceEntity> entities = taskStatusService.getAll();
        assertThat(entities).hasSize(7);
    }
}
