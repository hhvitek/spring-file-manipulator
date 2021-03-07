package spring.filemanipulator.utilities.string_filters.operations.squeeze;

import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.operations.CharacterOperation;

public class SqueezeCharacterFilter extends CharacterOperation {

    @Override
    protected String performOperation(@NotNull String input, char filterCharacter) {
        return stringAdditionalOperations.squeezeWhat(input, filterCharacter);
    }
}