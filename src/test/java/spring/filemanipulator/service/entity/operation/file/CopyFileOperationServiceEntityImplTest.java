package spring.filemanipulator.service.entity.operation.file;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class CopyFileOperationServiceEntityImplTest {

    @Autowired
    private CopyFileOperationServiceEntityImpl copyFileOperation;

    @Test
    public void messageOperationNameIsDefinedInPropertiesFileTest() {

        String expectedName = "COPY";

        assertThatNoException().isThrownBy(
                () -> copyFileOperation.getName()
        );
    }

    @Test
    public void messageOperationNameIsAsExpectedFileTest() {
        String expectedName = "COPY";

        String name = copyFileOperation.getName();
        assertThat(expectedName).isEqualTo(name);
    }

    @Test
    public void messageOperationNameCZExistsAndIsAsExpectedTest() {

        LocaleContextHolder.setLocale(Locale.forLanguageTag("cs-CZ"));

        String expectedName = "KOPÍROVÁNÍ";

        assertThatNoException().isThrownBy(
                () -> copyFileOperation.getName()
        );

        String actualName = copyFileOperation.getName();
        LocaleContextHolder.setLocale(Locale.getDefault());

        assertThat(expectedName).isEqualTo(actualName);
    }
}
