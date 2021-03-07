package spring.filemanipulator.utilities.operations.blacklist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.filemanipulator.utilities.string_filters.FilterException;
import spring.filemanipulator.utilities.string_filters.operations.blacklist.BlacklistRegexFilter;

public class BlacklistRegexFilterTest extends BlacklistFilterTest {

    @BeforeEach
    void init() {
        operation = new BlacklistRegexFilter();
    }

    @Test
    void oneWordFilterDoesUnderstandAsWholePatternTest() {
        final String input = "Hello World";
        final String outputExpected = "World";

        operation.addFilter("Hello ");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void allSpacesFilterTest() {
        final String input = "    He   l   l  o W    or    ld    ";
        final String outputExpected = "HelloWorld";

        operation.addFilter(" ");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void allWhitespacesFilterTest() {
        final String input = "    He   l   l  o W    or    ld    "
                + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t\t";
        final String outputExpected = "HelloWorld";

        operation.addFilter("\\s");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void onlyTwoAndMoreConsecutiveWhitespacesFilterTest() {
        final String input = "    He l l o    W    or    ld    "
                + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t\t";
        final String outputExpected = "He l l oWorld";

        operation.addFilter("\\s{2,}");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void malformedFilterRegexThrowsPatternException() {
        final String input = "Hello World";

        operation.addFilter("\\s");

        Assertions.assertThrows(
                FilterException.class,
                () -> operation.addFilter("\\s{}")
        );
    }

    @Test
    void oneWordTwoFilters_SecondFilterReplacesTheFirstOneTest() {
        final String input = "Hello World";
        final String outputExpected = "Hello ";

        operation.addFilter("Hello");
        operation.addFilter("World");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void oneLetterTwoFilters_SecondFilterReplacesTheFirstOneTest() {
        final String input = "Hello World";
        final String outputExpected = "Hell Wrld";

        operation.addFilter("l");
        operation.addFilter("o");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }
}