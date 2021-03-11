package spring.filemanipulator.utilities.string_filters.operations.whitelist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WhitelistRegexFilterTest extends WhitelistFilterTest {

    @BeforeEach
    void init() {
        operation = new WhitelistRegexFilter();
    }

    @Test
    void oneLetterOneFiltersWhitespacesTest() {
        final String input = "Hello World"
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t";
        final String expectedOutput = " "
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t";

        operation.addFilter("\\s");

        final String actualOutput = operation.filter(input);

        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void oneComplexRegexFilters_AllAlphaNumericANDUnderscoresTest() {
        final String input = "_Hello World 1453_"
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t";
        final String expectedOutput = "_HelloWorld1453_";

        operation.addFilter("[\\w_\\d]");

        final String actualOutput = operation.filter(input);

        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void oneLetterTwoFilters_SecondReplacesTheFirstOneTest() {
        final String input = "Hello World";
        final String expectedOutput = "lll";

        operation.addFilter("o");
        operation.addFilter("l");

        final String actualOutput = operation.filter(input);

        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}