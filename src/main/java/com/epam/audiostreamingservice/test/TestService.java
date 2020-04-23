package com.epam.audiostreamingservice.test;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TestService {

    @Async
    public CompletableFuture<String> testAsync() {
        return CompletableFuture.completedFuture(testSync());
    }

    public String testSync()  {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "TEST";
    }
}
