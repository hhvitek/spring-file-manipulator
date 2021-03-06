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
 * Allows filtering of results using request endpoint url parameters such as:
 * http://server:port/...api path.../search?filter=field:value
 *
 * Requires {@link AbstractSearchableRepository} interface based on JpaSpecificationExecutor.
 * This repository allows to execute Specifications based on the JPA criteria API
 *
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaSpecificationExecutor.html">JpaSpecificationExecutor</a>
 */
public abstract class AbstractSearchableRestController<Entity extends Serializable, ID extends Serializable> extends AbstractRestController<Entity, ID> {

    private static final Pattern searchCriteriaPattern = Pattern.compile(
            "(\\w+?)([:<>!~])(\\w+?),",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    protected final AbstractSearchableRepository<Entity, ID> searchableRepository;

    protected AbstractSearchableRestController(AbstractSearchableRepository<Entity, ID> searchableRepository) {
        super(searchableRepository);
        this.searchableRepository = searchableRepository;
    }

    // /search?filter=id:1,name:name_value
    @GetMapping("/search")
    public Collection<Entity> getManyBySearchFilter(@RequestParam(value = "filter", required = false) String filter) throws InvalidSearchFilterException {

        Collection<Entity> foundMany;
        if (filter != null) {
            foundMany = getManyUsingFilter(filter);
        } else {
            foundMany = getAll();
        }

        return foundMany;
    }

    private Collection<Entity> getManyUsingFilter(@NotNull String filter) throws InvalidSearchFilterException {
        try {
            CustomSpecificationCombineBuilder<Entity> builder = new CustomSpecificationCombineBuilder<>();
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

            Specification<Entity> specification = builder.build();
            return searchableRepository.findAll(specification);
        } catch (IllegalArgumentException ex) {
            throw new InvalidSearchFilterException("Invalid filter string: |" + filter + "|", ex.getMessage());
        }
    }

}