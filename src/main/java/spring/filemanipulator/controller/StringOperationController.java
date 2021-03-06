package spring.filemanipulator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/string_operations")
public class StringOperationController {

    @GetMapping
    public Collection<String> getAll() {
        return Collections.emptyList();
    }
}