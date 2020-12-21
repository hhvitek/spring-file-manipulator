package spring.filemanipulator.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DataJpaTest
// turn off default in-memory autoconfiguration, force to use .yml settings:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FileRegexPredefinedCategoryRepositoryTest {

    @Autowired
    private FileRegexPredefinedCategoryRepository repository;

    @BeforeEach
    public void init() {
        assertThat(repository).isNotNull();
    }

    @Test
    public void repositoryContainsTwoRecordsAfterInitialization() {
        List<FileRegexPredefinedCategoryEntity> fileRegexEntities = repository.findAll();

        int expectedRecordsCount = 2;
        assertThat(fileRegexEntities).hasSize(expectedRecordsCount);
    }

    @Test
    public void afterInitCorrectValuesInDbTest() {
        Optional<FileRegexPredefinedCategoryEntity> fileRegexEntityOpt = repository.findById(1);

        if (fileRegexEntityOpt.isPresent()) {
            assertThat(fileRegexEntityOpt.get()).hasFieldOrPropertyWithValue("pathMatcherSyntaxAndPattern", "regex:audio");
        } else {
            fail("Database does not contain file_regex with id=1. " +
                    "Sql script data.sql has not been executed properly?");
        }
    }

    @Test
    public void saveNewValueIntoDbTest() {
        FileRegexPredefinedCategoryEntity newEntity = new FileRegexPredefinedCategoryEntity("test-name", "test-regex");
        repository.save(newEntity);
        Integer newEntityId = newEntity.getId();

        assertThat(newEntityId).isNotNull();

        Optional<FileRegexPredefinedCategoryEntity> fromDbEntityOpt = repository.findById(newEntityId);
        fromDbEntityOpt.ifPresentOrElse(
                fileRegexEntity ->
                        assertThat(fileRegexEntity).usingRecursiveComparison()
                                .ignoringFields("id")
                                .isEqualTo(newEntity),
                () -> fail("Could not store and than retrieve file_regex from DB.")
        );
    }

    @Test
    public void repositoryIsSearchableTest() {
        assertThat(repository).isInstanceOf(AbstractSearchableRepository.class);
    }

}
