package spring.filemanipulator.utilities.string_operations.squeeze;

/**
 * {@code
 * Squeezes every squeezable sequence of characters.
 *
 * Squeezable sequence of character is defined as a consecutive sequence of the same characters
 * => It's char operation (char a == char b) returns true
 *
 * For example.
 * Input: aaXbbBBBcCCC
 * Output: aXbBcC
 * }
 */
public class SqueezeEverything extends StringSqueeze {

    @Override
    protected boolean isThisCharRelevantForSqueezing(char c) {
        return c == lastAddedChar;
    }
}