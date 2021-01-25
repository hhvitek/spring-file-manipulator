package spring.filemanipulator.service.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Getter
public class WorkerManagerOneWorkerItem {
    private final int workerId;
    private final Worker worker;
    private final CompletableFuture<Object> completableFuture;
}
