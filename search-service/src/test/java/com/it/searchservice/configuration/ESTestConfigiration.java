//package com.it.searchservice.configuration;
//
//import com.netflix.loadbalancer.Server;
//import com.netflix.loadbalancer.ServerList;
//import org.springframework.cloud.netflix.ribbon.StaticServerList;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.util.SocketUtils;
//import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
//import pl.allegro.tech.embeddedelasticsearch.PopularProperties;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//import java.util.UUID;
//
//@Configuration
//@Profile("test")
//public class ESTestConfigiration {
//
//    private EmbeddedElastic server;
//
//    public ESTestConfigiration() throws IOException {
//        int port_tcp = SocketUtils.findAvailableTcpPort();
//        int port_http = SocketUtils.findAvailableTcpPort();
//        server = EmbeddedElastic.builder()
//                .withElasticVersion(ELASTIC_VERSION)
//                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, port_tcp)
//                .withSetting(PopularProperties.HTTP_PORT, port_http)
//                .withSetting(PopularProperties.CLUSTER_NAME, UUID.randomUUID())
//                .withDownloadUrl(esUrl)
//                .build();
//    }
//
//    @PostConstruct
//    public void postConstruct() throws IOException, InterruptedException {
//        server.start();
//    }
//
//    @PreDestroy
//    public void preDestroy() {
//        server.stop();
//    }
//}
