package spring.filemanipulator.controller.specificationapi;

import org.junit.jupiter.api.Test;
import spring.filemanipulator.configuration.specificationapi.SearchOperation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class SearchOperationTest {

    @Test
    public void getSearchOperationByStringValueSuccessTest() {
        SearchOperation searchOperation = SearchOperation.getSearchOperationByStringValueOnErrorThrow(":");
        assertThat(searchOperation).isEqualTo(SearchOperation.EQUALITY);
    }

    @Test
    public void getSearchOperationByStringValueOnUnknownThrowsTest() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> {
                    SearchOperation.getSearchOperationByStringValueOnErrorThrow("Unknown Value");
                }
        );
    }

    @Test
    public void expectedNumericValueTest() {
        SearchOperation expectNumericValueOperation = SearchOperation.GREATER_THAN;
        SearchOperation expectNotNumericValueOperation = SearchOperation.EQUALITY;

        assertThat(expectNumericValueOperation.expectsNumericValue()).isTrue();
        assertThat(expectNotNumericValueOperation.expectsNumericValue()).isFalse();
    }

}