package spring.filemanipulator.utilities.string_filters.operations;

import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_operations.CustomStringAdditionalOperationsImpl;
import spring.filemanipulator.utilities.string_operations.StringAdditionalOperations;

public abstract class Operation {
    protected StringAdditionalOperations stringAdditionalOperations;

    protected Operation() {
        stringAdditionalOperations = new CustomStringAdditionalOperationsImpl();
    }

    public abstract @NotNull String filter(@NotNull String input);

    public abstract void addFilter(@NotNull String nextFilter);
}