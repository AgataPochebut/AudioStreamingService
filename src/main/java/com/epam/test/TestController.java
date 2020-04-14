package com.epam.test;

import com.epam.es_repository.SongElasticsearchRepository;
import com.epam.model.Song;
import org.asynchttpclient.*;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = "/client/{endpoint}")
    public void test(@PathVariable String endpoint) throws Exception {
        // execute a bound GET request
        AsyncHttpClientConfig clientConfig = Dsl.config().setConnectTimeout(15000).setRequestTimeout(15000).build();
        AsyncHttpClient HTTP_CLIENT = Dsl.asyncHttpClient(clientConfig);

        BoundRequestBuilder boundGetRequest = HTTP_CLIENT.prepareGet("http://localhost:8080/test/" + endpoint);

        ListenableFuture a = boundGetRequest.execute(new AsyncCompletionHandler<Integer>() {
            @Override
            public Integer onCompleted(Response response) {
                int resposeStatusCode = response.getStatusCode();
                System.out.println(resposeStatusCode);
                return resposeStatusCode;
            }
        });
    }

    @Autowired
    private TestService testService;

    @GetMapping(value = "/sync")
    public ResponseEntity<String> test1() {
        String result = testService.testSync();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/callable")
    public Callable<ResponseEntity<String>> test2() throws Exception {
        return () -> test1();
    }

    @GetMapping(value = "/deferred")
    public DeferredResult<ResponseEntity<String>> test3() throws Exception {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();

        CompletableFuture
                .supplyAsync(() -> test1())
                .whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
//        new Thread(() -> result.setResult(test1())).start();

        return deferredResult;
    }

    @GetMapping(value = "/future")
    public CompletableFuture<ResponseEntity<String>> test4() throws Exception {
        return CompletableFuture.supplyAsync(() -> test1());
    }


//    @Autowired
//    private RestHighLevelClient elasticsearchClient;
//
//    @PostMapping("/index/{index}/{type}/{id}")
//    public void test7(@PathVariable String index, @PathVariable String type, @PathVariable String id, @RequestBody String body) throws IOException {
//        IndexRequest request = new IndexRequest()
//                .index(index)
//                .type(type)
//                .id(id)
//                .source(body, XContentType.JSON);
//        IndexResponse indexResponse = elasticsearchClient.index(request, RequestOptions.DEFAULT);
//    }
//
//    @GetMapping("/search")
//    public void test12() throws IOException {
//        SearchRequest searchRequest = new SearchRequest();
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
//    }


    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @PostMapping("/index/template/{id}")
    public void test11(@PathVariable String id, @RequestBody Song body){

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id)
                .withObject(body)
                .build();

        elasticsearchTemplate.index(indexQuery);
    }

    @GetMapping("/search/template")
    public void test9(){
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        elasticsearchTemplate.queryForList(searchQuery, Song.class);
    }


    @Autowired
    private SongElasticsearchRepository elasticsearchRepository;

    @GetMapping("/search/repository")
    public void test10(){
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        List<Song> list = new ArrayList<>();
        elasticsearchRepository.search(searchQuery).forEach(i -> list.add(i));
    }
}
