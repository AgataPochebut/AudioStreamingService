package com.epam;

import org.asynchttpclient.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class TestControllerHttpClientTest {

    private static AsyncHttpClient HTTP_CLIENT;

    @BeforeAll
    public void setup() {
        AsyncHttpClientConfig clientConfig = Dsl.config().setConnectTimeout(15000).setRequestTimeout(15000).build();
        HTTP_CLIENT = Dsl.asyncHttpClient(clientConfig);
    }

    @Test
    public void testBasic() throws Exception {
        testSync("http://localhost:8080/songs");
    }

    @Test
    public void testCallable() throws Exception {
        testAsync("http://localhost:8080/songs/callable");
    }

    @Test
    public void testDeferred() throws Exception {
        testAsync("http://localhost:8080/songs/deferred");
    }

    @Test
    public void testFuture() throws Exception {
        testAsync("http://localhost:8080/songs/future");
    }

    public void testSync(String route) {

        BoundRequestBuilder boundGetRequest = HTTP_CLIENT.prepareGet(route);

        Future<Response> responseFuture = boundGetRequest.execute();
        try {
            Response response = responseFuture.get(5000, TimeUnit.MILLISECONDS);
            assertNotNull(response);
            assertEquals(200, response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testAsync(String route) throws ExecutionException, InterruptedException {

        // execute a bound GET request
        BoundRequestBuilder boundGetRequest = HTTP_CLIENT.prepareGet(route);

        boundGetRequest.execute(new AsyncCompletionHandler<Integer>() {
            @Override
            public Integer onCompleted(Response response) {
                int resposeStatusCode = response.getStatusCode();
                assertEquals(200, resposeStatusCode);
                return resposeStatusCode;
            }
        });
    }
}
