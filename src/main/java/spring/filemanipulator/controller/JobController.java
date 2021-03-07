package spring.filemanipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.filemanipulator.entity.JobEntity;
import spring.filemanipulator.repository.JobRepository;
import spring.filemanipulator.service.job.JobAlreadyFinishedException;
import spring.filemanipulator.service.job.JobNotFoundException;
import spring.filemanipulator.service.job.JobService;

@RestController
@RequestMapping("/api/jobs")
public class JobController extends AbstractSearchableRestController<JobEntity, Integer> {

    private final JobService jobService;

    private final JobRepository jobRepository;

    @Autowired
    protected JobController(final JobRepository jobRepository,
                            final JobService jobService) {
        super(jobRepository);
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id:\\d+}/signalToStop")
    public JobEntity signalToStop(@PathVariable Integer id) throws JobNotFoundException, JobAlreadyFinishedException {
        jobService.signalToStopIfNotFoundThrow(id);

        return jobRepository.findById(id).orElseThrow(
                () -> new JobNotFoundException(id)
        );
    }
}