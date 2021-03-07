package spring.filemanipulator.utilities.string_operations.squeeze;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Squeezes only squeezable sequences of all characters specified by regular expression.
 *
 * For example.
 * Input: aaXbbBBBcCCC
 * squeeze: [aB]
 * Output: aXbbBcCCC
 */
public class SqueezeSpecificCharRegexOnly extends StringSqueeze {

    private final Pattern squeezeRegexWhat;

    public SqueezeSpecificCharRegexOnly(@NotNull Pattern squeezeRegexWhat) {
        super();
        this.squeezeRegexWhat = squeezeRegexWhat;
    }

    @Override
    protected boolean isThisCharRelevantForSqueezing(char c) {
        Matcher matcher = squeezeRegexWhat.matcher(String.valueOf(c));
        return matcher.matches();
    }


}