package spring.filemanipulator.utilities.string_filters.operations.blacklist;


import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.operations.CharacterOperation;

public class BlacklistCharacterFilter extends CharacterOperation {

    @Override
    protected String performOperation(@NotNull String input, char filterCharacter) {
        return stringAdditionalOperations.replaceWhatTo(
                input,
                String.valueOf(filterCharacter),
                replaceWith
        );
    }
}