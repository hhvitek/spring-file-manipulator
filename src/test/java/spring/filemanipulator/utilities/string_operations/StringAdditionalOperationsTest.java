package spring.filemanipulator.utilities.string_operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.regex.Pattern;

public abstract class  StringAdditionalOperationsTest {

    StringAdditionalOperations additionalOperations;

    @Test
    void squeezeAllSpacesTest() {
        final String input = "     H   e         ll  o       Wor         l       d     ";
        final String outputExpected = " H e ll o Wor l d ";

        final String outputActual = additionalOperations.squeezeWhat(input, ' ');

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void squeezeAllRepeatedCharactersTest() {
        final String input = "   JjjjjJ   kkkk  k   ";
        final String outputExpected = " JjJ k k ";

        final String outputActual = additionalOperations.squeezeEverything(input);

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void squeezeAllWhitespacesTest() {
        final String input = "     H   e         ll  o       Wor         l       d     "
                + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t\t";

        final String outputExpected = " H e ll o Wor l d ";

        final String outputActual = additionalOperations.squeezeWhatRegex(input, Pattern.compile("\\s"));

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void squeezeTwoDifferentCharactersUsingRegexTest() {
        final String input = "aaXbbBBBcCCC";

        final String outputExpected = "aXbbBcCCC";

        final String outputActual = additionalOperations.squeezeWhatRegex(input, Pattern.compile("[aB]"));

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void squeezeTwoDifferentCharactersUsingTwoMethodCallsWithoutRegexTest() {
        final String input = "aaXbbBBBcCCC";

        final String outputExpected = "aXbbBcCCC";

        final String inputSqueezedBy_a = additionalOperations.squeezeWhat(input, 'a');
        final String inputSqueezedBy_a_B = additionalOperations.squeezeWhat(inputSqueezedBy_a, 'B');

        final String outputActual = inputSqueezedBy_a_B;

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void squeezeAllWhitespacesToUnderscoreTest() {
        final String input = "     H   e         ll  o       Wor         l       d     "
                + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t\t";

        final String outputExpected = "_H_e_ll_o_Wor_l_d_";

        final String outputWhitespacesSqueezed = additionalOperations.squeezeWhatRegex(input, Pattern.compile("\\s"));
        final String outputActual = additionalOperations.replaceWhatTo(outputWhitespacesSqueezed, " ", "_");

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    @EnabledOnOs({OS.WINDOWS})
    void replaceAllWhitespacesForUnderscoreWindowsOSTest() {
        final String input = "     H   e         ll  o       Wor         l       d"
                + System.lineSeparator() // on Windows contains two characters \r\n
                + System.lineSeparator()
                + System.lineSeparator();

        final String outputExpected = "_____H___e_________ll__o_______Wor_________l_______d______";

        final String outputActual = additionalOperations.replaceWhatRegexTo(input, Pattern.compile("\\s"), "_");

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    @EnabledOnOs({OS.LINUX})
    void replaceAllWhitespacesForUnderscoreLinuxOSTest() {
        final String input = "     H   e         ll  o       Wor         l       d"
                + System.lineSeparator() // on Windows contains two characters \r\n
                + System.lineSeparator()
                + System.lineSeparator();

        final String outputExpected = "_____H___e_________ll__o_______Wor_________l_______d___";

        final String outputActual = additionalOperations.replaceWhatRegexTo(input, Pattern.compile("\\s"), "_");

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void replaceAllSpacesForUnderscoreTest() {
        final String input = "     H   e         ll  o       Wor         l       d     "
                + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t\t";

        final String outputExpected = "_____H___e_________ll__o_______Wor_________l_______d_____"
                + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t\t";

        final String outputActual = additionalOperations.replaceWhatTo(input, " ", "_");

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test
    void replaceAllLineBreaksForUnderscoreTest() {
        final String input = " Hello World "
                + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "\t\t\t\t\t";

        final String outputExpected = " Hello World "
                + "___"
                + "\t\t\t\t\t";

        final String outputActual = additionalOperations.replaceWhatRegexTo(input, Pattern.compile(System.lineSeparator()), "_");

        Assertions.assertEquals(outputExpected, outputActual);
    }

    @Test void generateRandomAlphanumericString_resultIsNotNullTest() {
        int expectedOutputLength = 10;

        String actualOutput = additionalOperations.generateRandomAlphanumericString(expectedOutputLength);

        Assertions.assertNotNull(actualOutput);
    }

    @Test
    void generateRandomAlphanumericString_resultIsXXCharactersLong() {
        int expectedOutputLength = 10;

        String actualOutput = additionalOperations.generateRandomAlphanumericString(expectedOutputLength);
        int actualOutputLength = actualOutput.length();

        Assertions.assertEquals(expectedOutputLength, actualOutputLength);
    }

    @Test
    void generateRandomAlphanumericString_resultIsGeneratedEvenIfLengthParameterIsZeroTest() {
        String actualOutput = additionalOperations.generateRandomAlphanumericString(0);
        int actualOutputLength = actualOutput.length();

        Assertions.assertTrue(actualOutputLength > 0);
    }

}