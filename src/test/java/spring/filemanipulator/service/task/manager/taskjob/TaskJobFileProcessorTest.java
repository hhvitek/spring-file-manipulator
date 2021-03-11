package spring.filemanipulator.service.task.manager.taskjob;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.filemanipulator.entity.TaskEntity;
import spring.filemanipulator.service.entity.operation.file.AbstractFileOperationI18nNamedServiceEntity;
import spring.filemanipulator.service.entity.operation.file.CopyFileOperationI18nServiceEntityImpl;
import spring.filemanipulator.service.entity.operation.file.MoveFileOperationI18nServiceEntityImpl;
import spring.filemanipulator.service.entity.operation.string.AbstractStringOperationI18nNamedServiceEntity;
import spring.filemanipulator.service.entity.operation.string.NiceLookingFileNameStringOperationI18nImpl;
import spring.filemanipulator.service.entity.operation.string.ReplaceWhatRegexWithStringOperationI18nImpl;
import spring.filemanipulator.service.operation.file.FileOperationService;
import spring.filemanipulator.service.operation.file.FileOperationServiceImpl;
import spring.filemanipulator.service.operation.string.StringOperationService;
import spring.filemanipulator.service.operation.string.StringOperationServiceImpl;
import spring.filemanipulator.service.task.manager.TaskJobProcessingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


class TaskJobFileProcessorTest {

    private static final Path RESOURCE_PATH = Path.of("./src/test/resources/service/task/manager/taskjob");

    private FileOperationService fileOperationService;
    private StringOperationService stringOperationService;

    private TaskJobFileProcessor processor;

    public TaskJobFileProcessorTest() {
        AbstractFileOperationI18nNamedServiceEntity copyOperation = new CopyFileOperationI18nServiceEntityImpl(null);
        AbstractFileOperationI18nNamedServiceEntity moveOperation = new MoveFileOperationI18nServiceEntityImpl(null);

        fileOperationService = new FileOperationServiceImpl(List.of(copyOperation, moveOperation));

        AbstractStringOperationI18nNamedServiceEntity niceLookingFilename = new NiceLookingFileNameStringOperationI18nImpl(null);
        AbstractStringOperationI18nNamedServiceEntity replaceWhatRegexWith = new ReplaceWhatRegexWithStringOperationI18nImpl(null);

        stringOperationService = new StringOperationServiceImpl(List.of(niceLookingFilename, replaceWhatRegexWith));

        processor = new TaskJobFileProcessor(fileOperationService, stringOperationService);
    }

    @Test
    public void stringServiceWorksWithUniqueNameIds() {
        String uniqueNameId = NiceLookingFileNameStringOperationI18nImpl.UNIQUE_NAME_ID;
        Assertions.assertTrue(stringOperationService.existsByUniqueNameId(uniqueNameId));

        uniqueNameId = ReplaceWhatRegexWithStringOperationI18nImpl.UNIQUE_NAME_ID;
        Assertions.assertTrue(stringOperationService.existsByUniqueNameId(uniqueNameId));

        uniqueNameId = "NOT_EXISTING_ID";
        Assertions.assertFalse(stringOperationService.existsByUniqueNameId(uniqueNameId));
    }

    @Test
    public void processorNotInitializedFirstTest() {
        Assertions.assertThrows(
                TaskJobProcessingException.class,
                () -> processor.performAllOperations(Path.of("."))
        );

        Assertions.assertThrows(
                TaskJobProcessingException.class,
                () -> processor.performStringOperation(Path.of("."))
        );

        Assertions.assertThrows(
                TaskJobProcessingException.class,
                () -> processor.performFileOperation(Path.of("."), Path.of("."))
        );
    }

    @Test
    public void niceLookingFileNameTest() {
        String input = "nice,looking,file,name";
        String expectedOutput = "OUTPUT\\nice_looking_file_name";

        TaskEntity taskEntity = TaskEntity.builder()
                .fileOperationUniqueNameId("COPY")
                .fileOperationDestinationFolder("OUTPUT")
                .stringOperationUniqueNameId("NICE_LOOKING_FILENAME")
                .build();

        processor.initializeFromTaskEntity(taskEntity);

        Path outputFile = processor.performStringOperation(Path.of(input));

        Assertions.assertEquals(expectedOutput, outputFile.toString());
    }

    @Test
    public void renameExistingFileTest() throws IOException {
        recreateTestFolder();

        Path inputFile  = RESOURCE_PATH.resolve("input#file-1.in");
        Path expectedOutputFile = RESOURCE_PATH.resolve("OUTPUT/input#file-1.in");

        Assertions.assertTrue(
                Files.isRegularFile(inputFile)
        );

        Assertions.assertFalse(
                Files.isRegularFile(expectedOutputFile)
        );

        TaskEntity taskEntity = TaskEntity.builder()
                .fileOperationUniqueNameId("MOVE")
                .fileOperationDestinationFolder("OUTPUT")
                .stringOperationUniqueNameId("NICE_LOOKING_FILENAME")
                .build();

        processor.initializeFromTaskEntity(taskEntity);

        processor.performFileOperation(inputFile, expectedOutputFile);

        Assertions.assertFalse(
                Files.isRegularFile(inputFile)
        );

        Assertions.assertTrue(
                Files.isRegularFile(expectedOutputFile)
        );
    }

    private void recreateTestFolder() throws IOException {
        Path inputFile1  = RESOURCE_PATH.resolve("input#file-1.in");

        if (Files.isDirectory(RESOURCE_PATH)) {
            FileUtils.deleteDirectory(RESOURCE_PATH.toFile());
        }

        Files.createDirectory(RESOURCE_PATH);
        Files.createFile(inputFile1);
    }
}