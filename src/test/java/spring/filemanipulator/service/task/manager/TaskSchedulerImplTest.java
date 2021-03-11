package spring.filemanipulator.service.task.manager;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.repository.TaskRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// turn off default in-memory autoconfiguration, force to use .yml settings:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskSchedulerImplTest {

    private static final Path RESOURCE_PATH = Path.of("./src/test/resources/service/task/manager");
    private static final Path INPUT_FOLDER = RESOURCE_PATH.resolve("INPUT_FOLDER");
    private static final Path OUTPUT_FOLDER = RESOURCE_PATH.resolve("OUTPUT_FOLDER");

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JobRepository jobRepository;

    @BeforeAll
    public static void initBeforeAll() {
        Assertions.assertTrue(Files.isDirectory(RESOURCE_PATH), "File not found: " + RESOURCE_PATH);
    }

    @BeforeEach
    public void initBeforeEach() throws IOException {
       recreateTestFolder();
    }

    private void recreateTestFolder() throws IOException {
        Path inputFile1  = INPUT_FOLDER.resolve("input#file-1.in");
        Path inputFile2  = INPUT_FOLDER.resolve("input#file-2.in");

        if (Files.isDirectory(INPUT_FOLDER)) {
            FileUtils.deleteDirectory(INPUT_FOLDER.toFile());
        }

        Files.createDirectory(INPUT_FOLDER);
        Files.createFile(inputFile1);
        Files.createFile(inputFile2);

        if (Files.isDirectory(OUTPUT_FOLDER)) {
            FileUtils.deleteDirectory(OUTPUT_FOLDER.toFile());
        }

        Files.createDirectory(OUTPUT_FOLDER);
    }

    private ConditionFactory createAwaitility() {
        return Awaitility.await()
                .pollInterval(10L, TimeUnit.MILLISECONDS)
                .pollDelay(Duration.ZERO)
                .atMost(Duration.ofMillis(100L));
    }

    @Test
    public void scheduleAndStoreOk() {
        TaskEntity taskEntity = createTaskEntity();

        taskScheduler.scheduleAndStore(taskEntity);

        createAwaitility()
                .until(() -> taskEntity.getProcessedFileCount() > 1);

        Assertions.assertFalse(taskEntity.isHasError());
        Assertions.assertEquals(2, taskEntity.getProcessedFileCount());

        Assertions.assertTrue(
                Files.isRegularFile(OUTPUT_FOLDER.resolve("input_file-1.in"))
        );

        Assertions.assertTrue(
                Files.isRegularFile(OUTPUT_FOLDER.resolve("input_file-2.in"))
        );
    }

    private TaskEntity createTaskEntity() {
        TaskEntity taskEntity = TaskEntity.builder()
                .fileOperationUniqueNameId("COPY")
                .fileOperationInputFolder(INPUT_FOLDER.toString())
                .fileOperationDestinationFolder(OUTPUT_FOLDER.toString())
                .stringOperationUniqueNameId("NICE_LOOKING_FILENAME")
                .build();

        return taskEntity;
    }

    @Test
    public void nonExistentStringOperationTest() {
        TaskEntity taskEntity = createTaskEntity();
        taskEntity.setStringOperationUniqueNameId("NON_EXISTENT_NAME");

        taskScheduler.scheduleAndStore(taskEntity);

        createAwaitility()
                .until(taskEntity::isHasError);

        Assertions.assertTrue(taskEntity.isHasError());
        Assertions.assertTrue(taskEntity.getErrorMsg().contains("OperationNotFoundException"));
    }

    @Test
    public void nonExistentFileOperationTest() {
        TaskEntity taskEntity = createTaskEntity();
        taskEntity.setFileOperationUniqueNameId("NON_EXISTENT_NAME");

        taskScheduler.scheduleAndStore(taskEntity);

        createAwaitility()
                .until(taskEntity::isHasError);

        Assertions.assertTrue(taskEntity.isHasError());
        Assertions.assertTrue(taskEntity.getErrorMsg().contains("OperationNotFoundException"));
    }
}