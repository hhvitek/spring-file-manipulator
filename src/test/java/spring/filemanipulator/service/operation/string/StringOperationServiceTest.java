package spring.filemanipulator.service.operation.string;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.filemanipulator.service.entity.operation.string.AbstractStringOperationI18nNamedServiceEntity;
import spring.filemanipulator.service.entity.operation.string.NiceLookingFileNameStringOperationI18nImpl;
import spring.filemanipulator.service.entity.operation.string.ReplaceWhatRegexWithStringOperationI18nImpl;
import spring.filemanipulator.service.entity.operation.string.SqueezeCharsStringOperationI18nImpl;

import java.util.List;

@SpringBootTest(classes = {
        StringOperationServiceImpl.class,
        NiceLookingFileNameStringOperationI18nImpl.class,
        ReplaceWhatRegexWithStringOperationI18nImpl.class,
        SqueezeCharsStringOperationI18nImpl.class
})
class StringOperationServiceTest {

    @Autowired
    private StringOperationService stringOperationService;

    @Autowired
    private List<AbstractStringOperationI18nNamedServiceEntity> entities;

    @Test
    public void testGetAllEqualsThreeTest() {
        Assertions.assertThat(stringOperationService.getAllNamedEntities()).hasSize(3);
        Assertions.assertThat(stringOperationService.getAllUniqueNameIds()).hasSize(3);
    }

    @Test
    public void testContainsNiceLookingFileTest() {
        String expectedId = "NICE_LOOKING_FILENAME";

        Assertions.assertThat(stringOperationService.existsByUniqueNameId(expectedId)).isTrue();
    }

}