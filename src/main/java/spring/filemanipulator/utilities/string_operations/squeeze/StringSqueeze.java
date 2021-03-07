package spring.filemanipulator.utilities.string_operations.squeeze;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for squeeze operations.
 *
 * Squeezable sequence of character is defined as a consecutive sequence of the relevant characters
 *
 * In it's basic form relevant characters are the SAME characters:
 * => It's char operation (char a == char b) returns true
 *
 * For example.
 * Input: aaXbbBBBcCCC
 * Output: aXbBcC
 */
public abstract class StringSqueeze {

    private StringBuilder outputStringBuilder;

    protected char currentChar;
    protected char lastAddedChar;

    protected StringSqueeze() {
    }

    public String squeeze(@NotNull String input) {
        outputStringBuilder = createOutputStringBuilderWithMaxPossibleSize(input);

        if (isInputTooSmall(input)) {
            return input;
        }

        performSqueeze(input);

        return outputStringBuilder.toString();
    }

    private StringBuilder createOutputStringBuilderWithMaxPossibleSize(@NotNull String input) {
        return new StringBuilder(input.length());
    }

    private boolean isInputTooSmall(@NotNull String input) {
        return input.length() < 2;
    }

    private void performSqueeze(@NotNull String input) {
        char[] inputAsChars = input.toCharArray();

        char theFirstCharacter = getFirstChar(inputAsChars);
        outputStringBuilder.append(theFirstCharacter); // always append the first character

        lastAddedChar = theFirstCharacter;
        for (int i = 1; i < inputAsChars.length; i++) {
            currentChar = inputAsChars[i];
            if (shouldAppendChar(currentChar, lastAddedChar)) {
                outputStringBuilder.append(currentChar);
                lastAddedChar = currentChar;
            }
        }
    }

    private char getFirstChar(@NotNull char[] chars) {
        if (chars.length > 0) {
            return chars[0];
        } else {
            return '\0';
        }
    }

    private boolean shouldAppendChar(char currentChar, char lastAddedChar) {
        return !areThoseTwoConsecutiveSqueezableCharacters();
    }

    private boolean areThoseTwoConsecutiveSqueezableCharacters() {
        return isThisCharRelevantForSqueezing(currentChar)
                && isThisCharRelevantForSqueezing(lastAddedChar);
    }

    protected abstract boolean isThisCharRelevantForSqueezing(char c);
}