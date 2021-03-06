package spring.filemanipulator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.filemanipulator.entity.FileRegexPredefinedCategoryEntity;
import spring.filemanipulator.repository.FileRegexPredefinedCategoryRepository;

@RestController
@RequestMapping("/api/file_regex_predefined_categories")
public class FileRegexPredefinedCategoryController extends AbstractSearchableRestController<FileRegexPredefinedCategoryEntity, Integer> {

    public FileRegexPredefinedCategoryController(FileRegexPredefinedCategoryRepository repository) {
        super(repository);
    }

}