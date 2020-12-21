package spring.filemanipulator.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "task")
public class TaskEntity extends AbstractTimestampEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String taskStatusUniqueNameId;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String fileOperationUniqueNameId;

    private String fileOperationInputFolder;

    private String fileOperationDestinationFolder;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String stringOperationUniqueNameId;

    private String stringOperationRegexWhat;

    private String stringOperationReplaceTo;

    @Builder.Default
    @Column(nullable = false)
    private boolean hasError = false;
    @Builder.Default
    private String errorMsg = "";

    @Builder.Default
    @Column(nullable = false)
    private Integer totalFileCount = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer processedFileCount = 0;

    public void increaseProcessedFilesByOne() {
        processedFileCount++;
    }
}
