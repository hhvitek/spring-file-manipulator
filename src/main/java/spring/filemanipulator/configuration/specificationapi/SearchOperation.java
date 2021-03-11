package spring.filemanipulator.configuration.specificationapi;

import javax.validation.constraints.NotNull;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, CONTAINS;

    public static @NotNull SearchOperation getSearchOperationByStringValueOnErrorThrow(final String input) throws IllegalArgumentException {
        switch (input) {
            case ":":
                return EQUALITY;
            case "!":
                return NEGATION;
            case ">":
                return GREATER_THAN;
            case "<":
                return LESS_THAN;
            case "~":
                return CONTAINS;
            default:
                throw new IllegalArgumentException("Unknown search operation: " + input);
        }
    }

    public boolean expectsNumericValue() {
        return this == GREATER_THAN || this == LESS_THAN;
    }
}