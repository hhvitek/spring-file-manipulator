package spring.filemanipulator.utilities.string_filters.operations.squeeze;

import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.operations.RegexOperation;

import java.util.regex.Pattern;

public class SqueezeRegexFilter extends RegexOperation {
    @Override
    protected String performOperation(@NotNull String input, @NotNull Pattern filterPattern) {
        return stringAdditionalOperations.squeezeWhatRegex(input, filterPattern);
    }

}