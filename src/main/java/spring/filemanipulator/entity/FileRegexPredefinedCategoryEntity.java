package spring.filemanipulator.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Entity(name = "file_regex_predefined_category")
public class FileRegexPredefinedCategoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @NotBlank(message = "!uniqueNameId! is mandatory.")
    @Column(nullable = false, unique = true)
    private String uniqueNameId;

    private String description;

    @NonNull
    @NotBlank(message = "!pathMatcherSyntaxAndPattern! is mandatory.")
    @Column(nullable = false)
    private String pathMatcherSyntaxAndPattern;
}
