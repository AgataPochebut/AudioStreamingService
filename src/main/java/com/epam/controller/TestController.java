package com.epam.controller;

import org.asynchttpclient.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping("/test")
public class TestController {

//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/1", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public long upload1(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//        long start = System.currentTimeMillis();
//
//        Resource source = multipartFile.getResource();
//        File file = new File("/Java/temp", source.getFilename());
//        file.getParentFile().mkdirs();
//        FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));
//
//        long finish = System.currentTimeMillis();
//        long timeConsumedMillis = finish - start;
//        return timeConsumedMillis;
//    }


//
//    // Content type 'multipart/form-data;boundary
//    @PostMapping(value = "/2", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public long upload2(@RequestParam("data") MultipartFile multipartFile) throws Exception {
//        long start = System.currentTimeMillis();
//
//        Resource source = multipartFile.getResource();
//        File file = new File("/Java/temp", source.getFilename());
//        file.getParentFile().mkdirs();
//        FileUtils.copyInputStreamToFile(source.getInputStream(), file);
//
//        long finish = System.currentTimeMillis();
//        long timeConsumedMillis = finish - start;
//        return timeConsumedMillis;
//    }

    @GetMapping()
    public void test() throws Exception {
        // execute a bound GET request
        AsyncHttpClientConfig clientConfig = Dsl.config().setConnectTimeout(15000).setRequestTimeout(15000).build();
        AsyncHttpClient HTTP_CLIENT = Dsl.asyncHttpClient(clientConfig);

        BoundRequestBuilder boundGetRequest = HTTP_CLIENT.prepareGet("http://localhost:8080/test/future");

        ListenableFuture a = boundGetRequest.execute(new AsyncCompletionHandler<Integer>() {
            @Override
            public Integer onCompleted(Response response) {
                int resposeStatusCode = response.getStatusCode();
                System.out.println(resposeStatusCode);
                return resposeStatusCode;
            }
        });
    }

    @GetMapping(value = "/basic")
    public ResponseEntity<String> test1() throws InterruptedException {
        Thread.sleep(10000);
        return new ResponseEntity<>("TEST", HttpStatus.OK);
    }

    @GetMapping(value = "/callable")
    public Callable<ResponseEntity<String>> test2() throws Exception {
        return () -> {
            return test1();
        };
    }


    @GetMapping(value = "/deferred")
    public DeferredResult<ResponseEntity<String>> test3() throws Exception {
        DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
        result.onError(throwable -> System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXX " + throwable.getMessage()));
        result.onCompletion(() -> System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXX " + result.hasResult()));

        ForkJoinPool.commonPool().submit(() -> {
            try {
                test1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.setResult(ResponseEntity.ok("ok"));
        });

//        new Thread(() -> {
//            try {
//                result.setResult(test1());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

        return result;
    }

    @GetMapping(value = "/future")
    public CompletableFuture<ResponseEntity<String>> test4() throws Exception {
        return CompletableFuture.supplyAsync(()-> {
            try {
                return test1();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
