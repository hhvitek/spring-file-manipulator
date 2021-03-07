package spring.filemanipulator.utilities.operations.trim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.filemanipulator.utilities.string_filters.operations.trim.TrimCharacterFilter;

class TrimCharacterFilterTest extends TrimFilterTest {

    @BeforeEach
    void init() {
        operation = new TrimCharacterFilter();
    }

    @Test
    void oneLetterFilterTest() {
        final String input = "aHello Worldaa";
        final String outputExpected = "Hello World";

        operation.addFilter("a");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

}