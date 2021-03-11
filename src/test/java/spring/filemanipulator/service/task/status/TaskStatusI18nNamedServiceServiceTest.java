package spring.filemanipulator.service.task.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.filemanipulator.service.entity.NamedServiceEntity;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


//@SpringBootTest(classes = TaskStatusI18nNamedServiceService.class)
@ExtendWith(SpringExtension.class)
//@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
class TaskStatusI18nNamedServiceServiceTest {

    @Autowired
    private MessageSource messageSource;

    private TaskStatusI18nNamedServiceService taskStatusI18nNamedServiceService;

    @BeforeEach
    public void initBeforeEach() {
        taskStatusI18nNamedServiceService = new TaskStatusI18nNamedServiceService(messageSource);
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

    @Test
    public void existByUniqueNameTest() {
        String uniqueNameId_1 = "CREATED";
        String uniqueNameId_2 = "FINISHED_OK";

        assertThat(taskStatusI18nNamedServiceService.existsByUniqueName(uniqueNameId_1))
                .isTrue();

        assertThat(taskStatusI18nNamedServiceService.existsByUniqueName(uniqueNameId_2))
                .isTrue();
    }

    @Test
    public void doesNotExistByUniqueNameTest() {
        String uniqueNameId_Random = "RANDOM_NON_EXISTENT_STATUS";

        assertThat(taskStatusI18nNamedServiceService.existsByUniqueName(uniqueNameId_Random))
                .isFalse();
    }

    @Test
    public void czLocaleExistByUniqueNameTest() {
        String uniqueNameId_Random = "RANDOM_NON_EXISTENT_STATUS";

        assertThat(taskStatusI18nNamedServiceService.existsByUniqueName(uniqueNameId_Random))
                .isFalse();
    }
}