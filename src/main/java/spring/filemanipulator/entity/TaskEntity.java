package spring.filemanipulator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import spring.filemanipulator.service.task.TaskNotScheduledException;
import spring.filemanipulator.service.task.status.TaskStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Represents the actual implementation of Job => called Task => TaskJob in the app code base.
 */
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

    @Builder.Default
    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String taskStatusUniqueNameId = TaskStatusEnum.CREATED.name();

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

    /**
     * Any task should be scheduled and executed at some point in time.
     * This is one-to-one relationship. One task one job.
     * If an user wants a task with same parameters as before, the user must create a new task...
     *
     * This can easily be implemented as a foreign key or as a join table.
     */

    /*
     * USING a join table
    */
    @JsonIgnoreProperties({"hibernate_lazy_initializer"})
    @Builder.Default
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_to_scheduled_job_mapping",
            joinColumns =
                @JoinColumn(
                        name = "fk_task_id",
                        referencedColumnName = "id"
                ),
            inverseJoinColumns =
                @JoinColumn(
                        name = "fk_job_id",
                        referencedColumnName = "id"
                )
    )
    private JobEntity jobEntity = null;

    @JsonIgnore // method starting with "get", let Jackson ignore this
    public int getJobIdIfNotScheduledThrow() throws TaskNotScheduledException {
        if (!hasBeenScheduled()) {
            throw new TaskNotScheduledException(id);
        }
        return jobEntity.getId();
    }

    public boolean hasBeenScheduled() {
        return jobEntity != null;
    }

    public void increaseProcessedFilesByOne() {
        processedFileCount++;
    }
}