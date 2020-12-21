package spring.filemanipulator.specificationapi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;


class SearchCriteriaTest {

    @Test
    void createSearchCriteriaFromValuesSuccessTest() {
        String key = "id";
        SearchOperation searchOperation = SearchOperation.EQUALITY;
        String value = "1";

        assertThatNoException().isThrownBy( () -> {
            SearchCriteria.from(key, searchOperation, value);
        });
    }

    @Test
    void createSearchCriteriaFromValuesOnErrorThrowTest() {
        String key = "id";
        SearchOperation searchOperation = SearchOperation.GREATER_THAN;
        String value = "xyx";

        assertThatIllegalArgumentException().isThrownBy( () ->
            SearchCriteria.from(key, searchOperation, value)
        );
    }
}
