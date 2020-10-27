package com.it.songservice.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.util.SocketUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@Configuration
@Profile("test")
public class RibbonTestConfiguration {

    private int port;

    private WireMockServer wireMockServer;

    public RibbonTestConfiguration() throws IOException {
        port = SocketUtils.findAvailableTcpPort();
        wireMockServer = new WireMockServer(port);
        WireMock.configureFor(port);
    }

    @PostConstruct
    public void postConstruct() {
        wireMockServer.start();

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlPathMatching(("/songs/index")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.delete(urlPathMatching(("/songs/delete")))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));
    }

    @PreDestroy
    public void preDestroy() {
        wireMockServer.stop();
    }

    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(new Server("localhost", port));
    }

}
