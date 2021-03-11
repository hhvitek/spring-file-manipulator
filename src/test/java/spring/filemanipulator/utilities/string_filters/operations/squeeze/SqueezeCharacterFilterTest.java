package spring.filemanipulator.utilities.string_filters.operations.squeeze;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqueezeCharacterFilterTest extends SqueezeFilterTest {

    @BeforeEach
    void init() {
        operation = new SqueezeCharacterFilter();
    }

    @Test
    void twoFiltersOneLetterFilterTest() {
        final String input = "Hello Woorldddd";
        final String outputExpected = "Helo Worldddd";

        operation.addFilter("l");
        operation.addFilter("o");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void oneFilterTwoCharacters() {
        final String input = "Hello Woorldddd";
        final String outputExpected = "Helo Worldddd";

        operation.addFilter("lo");

        final String outputActual = operation.filter(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

}