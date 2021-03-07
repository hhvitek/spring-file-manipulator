package spring.filemanipulator.utilities.string_filters.operations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CharacterOperation extends Operation {

    protected List<Character> filterCharacters;
    protected String replaceWith;

    protected CharacterOperation() {
        filterCharacters = new ArrayList<>();
        replaceWith = "";
    }

    @Override
    public @NotNull String filter(@NotNull String input) {
        String output = input;
        for (char filterCharacter : filterCharacters) {
            output = performOperation(output, filterCharacter);
        }
        return output;
    }

    public void replaceWith(@NotNull String replaceWith) {
        this.replaceWith = replaceWith;
    }

    public void addFilter(char filterCharacter) {
        filterCharacters.add(filterCharacter);
    }

    @Override
    public void addFilter(@NotNull String filterCharacters) {
        char[] filterCharactersAsCharArray = filterCharacters.toCharArray();
        for (char filteredCharacter : filterCharactersAsCharArray) {
            addFilter(filteredCharacter);
        }
    }

    protected abstract String performOperation(@NotNull String input, char filterCharacter);
}