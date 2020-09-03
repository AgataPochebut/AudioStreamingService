package com.epam.songservice.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.SocketUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

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
