package spring.filemanipulator.utilities.string_filters.operations.filename;

import org.jetbrains.annotations.NotNull;
import spring.filemanipulator.utilities.string_filters.FilterException;
import spring.filemanipulator.utilities.string_filters.operations.Operation;
import spring.filemanipulator.utilities.string_filters.operations.chain_of_operations.ChainOfOperations;
import spring.filemanipulator.utilities.string_filters.operations.squeeze.SqueezeCharacterFilter;
import spring.filemanipulator.utilities.string_filters.operations.trim.TrimCharacterFilter;
import spring.filemanipulator.utilities.string_filters.operations.whitelist.WhitelistRegexFilter;

import static spring.filemanipulator.utilities.string_filters.ErrorCode.UNSUPPORTED_OPERATION;


public class FileNameFilterOperation extends Operation {

    private ChainOfOperations operationsChain;

    public FileNameFilterOperation() {
        operationsChain = createFileNameOperationsChain();
    }

    private ChainOfOperations createFileNameOperationsChain() {
        operationsChain = new ChainOfOperations();

        WhitelistRegexFilter whitelistRegexFilter = new WhitelistRegexFilter();
        whitelistRegexFilter.addFilter("a-zA-Z0-9_\\-\\)\\(\\]\\[\\+");
        whitelistRegexFilter.replaceWith("_");
        operationsChain.addOperation(whitelistRegexFilter);

        SqueezeCharacterFilter squeezeCharacterFilter = new SqueezeCharacterFilter();
        squeezeCharacterFilter.addFilter('_');
        operationsChain.addOperation(squeezeCharacterFilter);

        TrimCharacterFilter trimCharacterFilter = new TrimCharacterFilter();
        trimCharacterFilter.addFilter('_');
        operationsChain.addOperation(trimCharacterFilter);

        return operationsChain;
    }

    @Override
    public @NotNull String filter(@NotNull String input) {
        return operationsChain.filter(input);
    }

    @Override
    public void addFilter(@NotNull String nextFilter) {
        throw new FilterException(UNSUPPORTED_OPERATION, "FileNameFilterOperation.addFilter()");
    }
}