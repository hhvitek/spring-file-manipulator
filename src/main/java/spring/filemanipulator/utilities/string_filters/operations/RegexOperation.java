package spring.filemanipulator.utilities.string_filters.operations;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.filemanipulator.utilities.string_filters.FilterException;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static spring.filemanipulator.utilities.string_filters.ErrorCode.ILLEGAL_FILTER_PATTERN_SYNTAX;


public abstract class RegexOperation extends Operation {

    private static final Logger logger = LoggerFactory.getLogger(RegexOperation.class);

    protected String replaceWith;

    protected Pattern filterPattern;

    protected RegexOperation() {
        replaceWith = "";
    }

    protected RegexOperation(@NotNull Pattern filterPattern) throws FilterException {
        this();
        addFilter(filterPattern);
    }

    public void addFilter(@NotNull Pattern newFilterPattern) throws FilterException {
        filterPattern = newFilterPattern;
    }

    @Override
    public void addFilter(@NotNull String newFilter) throws FilterException {
        addFilter(compileStringRepresentationOfPattern(newFilter));
    }

    protected Pattern compileStringRepresentationOfPattern(@NotNull String patternRepresentation) throws FilterException {
        try {
            return Pattern.compile(patternRepresentation);
        } catch (PatternSyntaxException ex) {
            logger.error("Illegal filter pattern syntax: {}", patternRepresentation);
            throw new FilterException(ILLEGAL_FILTER_PATTERN_SYNTAX, patternRepresentation);
        }
    }

    public void replaceWith(@NotNull String replaceWith) {
        this.replaceWith = replaceWith;
    }

    @Override
    public @NotNull String filter(@NotNull String input) {
        if (!hasAnyPattern()) {
            logger.warn("No regex filter has been set! No changes to input string done!");
            return input;
        }

        return performOperation(input, filterPattern);
    }

    protected boolean hasAnyPattern() {
        return (filterPattern != null);
    }

    protected abstract String performOperation(@NotNull String input, @NotNull Pattern filterPattern);
}