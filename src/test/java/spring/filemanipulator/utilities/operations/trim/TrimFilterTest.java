package spring.filemanipulator.utilities.operations.trim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.filemanipulator.utilities.string_filters.operations.Operation;

abstract public class TrimFilterTest {

    Operation operation;

    @Test
    void noFilterShouldBehaveAsSpaceTest() {
        final String input = "  Hello World   ";
        final String outputExpected = "Hello World";

        final String outputActual = operation.filter(input);
        Assertions.assertEquals(outputExpected, outputActual);
    }


}