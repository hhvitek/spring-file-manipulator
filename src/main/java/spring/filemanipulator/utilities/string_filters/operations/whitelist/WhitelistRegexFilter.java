package spring.filemanipulator.utilities.string_filters.operations.whitelist;

import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.FilterException;
import spring.filemanipulator.utilities.string_filters.operations.ModifiedRegexOperation;

import java.util.regex.Pattern;

public class WhitelistRegexFilter extends ModifiedRegexOperation {

    @Override
    protected String performOperation(@NotNull String input, @NotNull Pattern filterPattern) {
        return stringAdditionalOperations.replaceWhatRegexTo(input, modifiedFilterPattern, replaceWith);
    }

    @Override
    protected Pattern modifyPattern(@NotNull Pattern patternToBeNegated) throws FilterException {
        String negatePatternAsString = negatePattern(patternToBeNegated.pattern());
        return compileStringRepresentationOfPattern(negatePatternAsString);
    }

    private String negatePattern(@NotNull String patternToBeNegated) {
        return "[^" + patternToBeNegated + "]";
    }
}