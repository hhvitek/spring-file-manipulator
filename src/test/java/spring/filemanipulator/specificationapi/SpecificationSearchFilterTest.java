package spring.filemanipulator.specificationapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;
import spring.filemanipulator.repository.FileRegexPredefinedCategoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// turn off default in-memory autoconfiguration, force to use .yml settings:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpecificationSearchFilterTest {

    @Autowired
    FileRegexPredefinedCategoryRepository repository;


    @Test
    public void basicRepositorySearchTest() {

        FileRegexPredefinedCategoryEntity newEntity = new FileRegexPredefinedCategoryEntity("new-name", "new-regex");
        repository.save(newEntity);
        Integer newEntityId = newEntity.getId();
        assertThat(newEntityId).isNotNull();

        CustomSpecification<FileRegexPredefinedCategoryEntity> specification1 = new CustomSpecification(
                SearchCriteria.from(
                    "id",
                        SearchOperation.EQUALITY,
                        String.valueOf(newEntityId)
                )
        );

        List<FileRegexPredefinedCategoryEntity> foundEntities = repository.findAll(specification1);
        assertThat(foundEntities).hasSize(1);

        FileRegexPredefinedCategoryEntity foundEntity = foundEntities.get(0);
        assertThat(foundEntity).usingRecursiveComparison().ignoringFields("id").isEqualTo(newEntity);
    }

    @Test
    public void advancedRepositorySearchTest() {

        FileRegexPredefinedCategoryEntity newEntity = new FileRegexPredefinedCategoryEntity("new-name", "new-regex");
        repository.save(newEntity);
        Integer newEntityId = newEntity.getId();
        assertThat(newEntityId).isNotNull();


        CustomSpecificationCombineBuilder builder = new CustomSpecificationCombineBuilder();
        builder.with("uniqueNameId", SearchOperation.CONTAINS, "new");
        builder.with("id", SearchOperation.GREATER_THAN, "1");
        builder.with("pathMatcherSyntaxAndPattern", SearchOperation.EQUALITY, "new-regex");

        Specification<FileRegexPredefinedCategoryEntity> specification = builder.build();

        List<FileRegexPredefinedCategoryEntity> foundEntities = repository.findAll(specification);
        assertThat(foundEntities).hasSize(1);

        FileRegexPredefinedCategoryEntity foundEntity = foundEntities.get(0);
        assertThat(foundEntity).usingRecursiveComparison().ignoringFields("id").isEqualTo(newEntity);
    }

    @Test
    public void foundNothingAdvancedRepositorySearchTest() {

        FileRegexPredefinedCategoryEntity newEntity = new FileRegexPredefinedCategoryEntity("new-name", "new-regex");
        repository.save(newEntity);
        Integer newEntityId = newEntity.getId();
        assertThat(newEntityId).isNotNull();


        CustomSpecificationCombineBuilder builder = new CustomSpecificationCombineBuilder();
        builder.with("uniqueNameId", SearchOperation.CONTAINS, "new");
        builder.with("id", SearchOperation.LESS_THAN, "1");
        builder.with("pathMatcherSyntaxAndPattern", SearchOperation.EQUALITY, "new-regex");

        Specification<FileRegexPredefinedCategoryEntity> specification = builder.build();

        List<FileRegexPredefinedCategoryEntity> foundEntities = repository.findAll(specification);
        assertThat(foundEntities).hasSize(0);
    }

}
