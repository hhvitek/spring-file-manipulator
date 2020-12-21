package spring.filemanipulator.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.filemanipulator.controller.error.InvalidSearchFilterException;
import spring.filemanipulator.repository.AbstractSearchableRepository;
import spring.filemanipulator.specificationapi.CustomSpecificationCombineBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows filtering of results using request endpoint url parameters.
 * http:/.../search?filter=field:value
 */
public abstract class AbstractSearchableRestController<ID extends Serializable, E extends Serializable> extends AbstractRestController<ID, E> {

    private static final Pattern searchCriteriaPattern = Pattern.compile(
            "(\\w+?)([:<>!~])(\\w+?),",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    protected final AbstractSearchableRepository<ID, E> searchableRepository;

    protected AbstractSearchableRestController(AbstractSearchableRepository<ID, E> searchableRepository) {
        super(searchableRepository);
        this.searchableRepository = searchableRepository;
    }

    // /search?filter=id:1,name:name_value
    @GetMapping(value = "/search")
    public Collection<E> getManyBySearchFilter(@RequestParam(value = "filter", required = false) String filter) throws InvalidSearchFilterException {

        Collection<E> foundMany;
        if (filter != null) {
            foundMany = getManyUsingFilter(filter);
        } else {
            foundMany = getAll();
        }

        return foundMany;
    }

    private Collection<E> getManyUsingFilter(@NotNull String filter) throws InvalidSearchFilterException {
        try {
            CustomSpecificationCombineBuilder<E> builder = new CustomSpecificationCombineBuilder<>();
            Matcher matcher = searchCriteriaPattern.matcher(filter + ",");
            if (matcher.find()) {
                // may throw Illegal argument exception
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            } else {
                // first time if not match must throw error: /search?filter=
                // missing actual filter content
                throw new IllegalArgumentException("Filter must be in the following format: <field><operation><value>.");
            }

            while (matcher.find()) { // additional filters
                // may throw Illegal argument exception
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }

            Specification<E> specification = builder.build();
            return searchableRepository.findAll(specification);
        } catch (IllegalArgumentException ex) {
            throw new InvalidSearchFilterException("Invalid filter string: |" + filter + "|", ex.getMessage());
        }
    }

}
