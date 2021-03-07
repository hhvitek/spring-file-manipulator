package spring.filemanipulator.utilities.string_filters.operations;

import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.FilterException;

import java.util.regex.Pattern;

public abstract class ModifiedRegexOperation extends RegexOperation {

    protected Pattern modifiedFilterPattern;

    @Override
    public void addFilter(@NotNull Pattern newFilter) throws FilterException {
        super.addFilter(newFilter);
        patternHasChanged();
    }

    protected void patternHasChanged() {
        modifiedFilterPattern = modifyPattern(filterPattern);
    }

    protected abstract Pattern modifyPattern(@NotNull Pattern filterPattern);
}