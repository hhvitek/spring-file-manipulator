package spring.filemanipulator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "settings")
public class SettingsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sourceFolder;
    private String destinationFolder;

    @ManyToOne
    @JoinColumn(name = "fk_selected_file_regex_predefined_category_id")
    @JsonProperty("selected_file_regex_predefined_category")
    private FileRegexPredefinedCategoryEntity selectedFileRegexPredefinedCategoryEntity;

    private String selectedStringOperationUniqueNameId;
}
