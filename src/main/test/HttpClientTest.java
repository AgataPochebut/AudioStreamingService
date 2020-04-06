import org.asynchttpclient.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class HttpClientTest {

    private static AsyncHttpClient HTTP_CLIENT;

    @Before
    public void setup() {
        AsyncHttpClientConfig clientConfig = Dsl.config().setConnectTimeout(15000).setRequestTimeout(15000).build();
        HTTP_CLIENT = Dsl.asyncHttpClient(clientConfig);
    }

    @Test
    public void testSync() {

        BoundRequestBuilder boundGetRequest = HTTP_CLIENT.prepareGet("http://www.baeldung.com");

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

    @Test
    public void testAsync() throws ExecutionException, InterruptedException {

        // execute a bound GET request
        BoundRequestBuilder boundGetRequest = HTTP_CLIENT.prepareGet("http://www.baeldung.com");

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
