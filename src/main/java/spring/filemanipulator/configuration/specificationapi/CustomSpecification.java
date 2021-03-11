package spring.filemanipulator.configuration.specificationapi;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public class CustomSpecification<E extends Serializable> implements Specification<E> {

    private final SearchCriteria criteria;

    public CustomSpecification(final SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(final @NotNull Root<E> root, final @NotNull CriteriaQuery<?> query, final @NotNull CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue());
            case LESS_THAN:
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }

}