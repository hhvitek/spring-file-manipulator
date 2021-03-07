package spring.filemanipulator.utilities.string_operations;


import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_operations.squeeze.SqueezeEverything;
import spring.filemanipulator.utilities.string_operations.squeeze.SqueezeSpecificCharOnly;
import spring.filemanipulator.utilities.string_operations.squeeze.SqueezeSpecificCharRegexOnly;
import spring.filemanipulator.utilities.string_operations.squeeze.StringSqueeze;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Only Java SE API
 * 3rd party libraries for manipulating String NOT USED:
 *      - Apache Commons's StringUtils and RegExUtils
 *      - Google's Guava
 */
public class CustomStringAdditionalOperationsImpl implements StringAdditionalOperations {
    @Override
    public String replaceWhatTo(@NotNull String input, @NotNull String replaceWhat, @NotNull String replaceWith) {
        return input.replace(replaceWhat, replaceWith);
    }

    @Override
    public String replaceWhatRegexTo(@NotNull String input, @NotNull Pattern regexWhat, @NotNull String replaceWith) {
        return input.replaceAll(regexWhat.pattern(), replaceWith);
    }

    @Override
    public String squeezeWhatRegex(@NotNull String input, @NotNull Pattern regexWhat) {
        StringSqueeze squeeze = new SqueezeSpecificCharRegexOnly(regexWhat);
        return squeeze.squeeze(input);
    }

    @Override
    public String squeezeEverything(@NotNull String input) {
        StringSqueeze squeeze = new SqueezeEverything();
        return squeeze.squeeze(input);
    }

    @Override
    public String squeezeWhat(@NotNull String input, char squeezeWhat) {
        StringSqueeze squeeze = new SqueezeSpecificCharOnly(squeezeWhat);
        return squeeze.squeeze(input);
    }

    /**
     * Source code from: https://www.baeldung.com/java-random-string
     *
     * @param length Length of generated String. If the length is less or equal zero the new length is chosen...at random.
     */
    @Override
    public String generateRandomAlphanumericString(int length) {
        if (length <= 0) {
            length = new Random().nextInt(127) + 1;
        }

        return generateRandomAlphanumericStringOfExactLength(length);
    }

    private String generateRandomAlphanumericStringOfExactLength(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        SecureRandom random = new SecureRandom(); // secure random crypto secure -- just to show difference to Random()

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}