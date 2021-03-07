package spring.filemanipulator.utilities.string_filters.operations.trim;

import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.ErrorCode;
import spring.filemanipulator.utilities.string_filters.FilterException;
import spring.filemanipulator.utilities.string_filters.operations.CharacterOperation;

public class TrimCharacterFilter extends CharacterOperation {

    private String trimmingString;

    public TrimCharacterFilter() {
        trimmingString = " ";
    }

    @Override
    public  void addFilter(char newFilter) {
        addFilter(String.valueOf(newFilter));
    }

    @Override
    public void addFilter(@NotNull String newFilter) {
        trimmingString = newFilter;
    }

    @Override
    protected String performOperation(@NotNull String input, char filterCharacter) {
        throw new FilterException(ErrorCode.UNSUPPORTED_OPERATION, "TrimCharacterFilter.performOperation()");
    }

    @Override
    public @NotNull String filter(@NotNull String input) {
        String output = stripLeading(input);
        output = stripTrailing(output);
        return output;
    }

    private String stripLeading(@NotNull String input) {
        String output = input;
        while(output.startsWith(trimmingString)) {
            output = output.substring(trimmingString.length());
        }
        return output;
    }

    private String stripTrailing(@NotNull String input) {
        String output = input;
        while (output.endsWith(trimmingString)) {
            output = output.substring(0, output.length() - trimmingString.length());
        }
        return output;
    }
}