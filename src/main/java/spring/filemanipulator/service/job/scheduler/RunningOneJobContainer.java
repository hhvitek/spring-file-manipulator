package spring.filemanipulator.service.job.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import spring.filemanipulator.service.job.Job;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
public class RunningOneJobContainer {
    private final int id;
    private final Job job;
    @Setter
    private CompletableFuture<Object> completableFuture;
}