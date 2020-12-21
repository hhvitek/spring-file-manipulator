package spring.filemanipulator.specificationapi;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public final class SearchCriteria {

    private String key;
    private SearchOperation operation;
    private String value;

    private SearchCriteria() {
    }

    public boolean isOrPredicate() {
        return false;
    }

    /**
     * Instantiate SearchCriteria from values: |key|operation|value| such as: id:12
     * @param key Field name such as id, name, description, ...
     * @param operation Operation that should be performed with key and value.
     * @param value Value associated with key value.

     * @throws IllegalArgumentException if operation and value are semantically incompatible. Operations such as
     * greater than or lesser than must have numerical values...
     */
    public static @NotNull SearchCriteria from(String key, SearchOperation operation, String value) throws IllegalArgumentException {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.key = key;
        searchCriteria.operation = operation;

        if (operation.expectsNumericValue()) {
            if (!isValueNumeric(value)) {
                throw new IllegalArgumentException("Invalid value. Operation: " + operation + " requires numeric value but: "+ value + " found.");
            }
        }
        searchCriteria.value = value;
        return searchCriteria;
    }

    /**
     * Same as {@link #from(String, SearchOperation, String)}. Just operation parameter is translated into SearchOperation, on
     * error throws IllegalArgumentException.
     */
    public static @NotNull SearchCriteria from(String key, String operation, String value) throws IllegalArgumentException {
        SearchOperation searchOperation = SearchOperation.getSearchOperationByStringValueOnErrorThrow(operation);
        return from(key, searchOperation, value);
    }

    private static boolean isValueNumeric(final String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException | NullPointerException ex) {
            return false;
        }
    }


}
