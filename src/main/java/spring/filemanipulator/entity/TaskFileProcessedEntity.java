package spring.filemanipulator.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "task_file_processed")
public class TaskFileProcessedEntity extends AbstractTimestampEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_task_id", nullable = false)
    private TaskEntity taskEntity;

    @NonNull
    @NotNull
    @Column(nullable = false)
    private String sourceFile;

    @NonNull
    @NotNull
    @Column(nullable = false)
    private String destinationFile;

    @Column(nullable = false)
    private boolean hasError = false;

    private String errorMsg = "";

    public TaskFileProcessedEntity(TaskEntity taskEntity) {
        this.taskEntity = taskEntity;
    }


}