package spring.filemanipulator.specificationapi;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class CustomSpecificationCombineBuilder<E extends Serializable> {

    private final List<SearchCriteria> searchCriterias;

    public CustomSpecificationCombineBuilder() {
        searchCriterias = new ArrayList<>();
    }

    // API

    public CustomSpecificationCombineBuilder<E> with(final String key, final String operation, final String value) throws IllegalArgumentException {
        searchCriterias.add(SearchCriteria.from(key, operation, value));
        return this;
    }

    public CustomSpecificationCombineBuilder<E> with(final String key, final SearchOperation operation, final String value) throws IllegalArgumentException {
        searchCriterias.add(SearchCriteria.from(key, operation, value));
        return this;
    }

    public Specification<E> build() {
        if (searchCriterias.isEmpty())
            return null;

        Specification<E> result = new CustomSpecification<>(searchCriterias.get(0));

        for (int i = 1; i < searchCriterias.size(); i++) {
            result = searchCriterias.get(i).isOrPredicate() // not implemented always and
                    ? Specification.where(result).or(new CustomSpecification<>(searchCriterias.get(i)))
                    : Specification.where(result).and(new CustomSpecification<>(searchCriterias.get(i)));
        }

        return result;
    }
}
