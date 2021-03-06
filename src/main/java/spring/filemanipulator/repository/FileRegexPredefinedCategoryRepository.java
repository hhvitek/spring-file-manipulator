package spring.filemanipulator.repository;

import org.springframework.stereotype.Repository;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;

@Repository
public interface FileRegexPredefinedCategoryRepository extends AbstractSearchableRepository<FileRegexPredefinedCategoryEntity, Integer> {
}