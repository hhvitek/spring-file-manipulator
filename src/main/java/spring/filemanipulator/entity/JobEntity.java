package spring.filemanipulator.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import spring.filemanipulator.service.job.JobStatusEnum;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents scheduled, running, finished Job.
 *
 * This should be independent of any actual Job implementation.
 * There should be just general Job data related to ALL Job implementations.
 */
@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "job")
public class JobEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(nullable = false)
    private JobStatusEnum jobStatusUniqueNameId = JobStatusEnum.CREATED;

    public static JobEntity createNewWithoutId() {
        return new JobEntity();
    }

}