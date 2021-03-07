package spring.filemanipulator.utilities.string_filters.operations.whitelist;


import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.FilterException;
import spring.filemanipulator.utilities.string_filters.operations.CharacterOperation;

import static spring.filemanipulator.utilities.string_filters.ErrorCode.UNSUPPORTED_OPERATION;


public class WhitelistCharacterFilter extends CharacterOperation {

    @Override
    public @NotNull String filter(@NotNull String input) {
        char[] inputAsArray = input.toCharArray();
        StringBuilder outputBuilder = new StringBuilder(inputAsArray.length);

        for(char character: inputAsArray) {
            if (filterCharacters.contains(character)) {
                outputBuilder.append(character);
            }
        }
        return outputBuilder.toString();
    }

    @Override
    protected String performOperation(@NotNull String input, char filterCharacter) {
        throw new FilterException(UNSUPPORTED_OPERATION, "WhitelistCharacterFilter.performOperation()");
    }
}