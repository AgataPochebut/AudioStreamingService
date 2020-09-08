package com.it.songservice.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.SocketUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@Profile("test")
public class S3TestConfiguration {

    @Value("${s3.defaultBucket}")
    private String defaultBucketName;

    private int port;
    private S3Mock api;

    public S3TestConfiguration()
    {
        port = SocketUtils.findAvailableTcpPort();
        api = new S3Mock.Builder().withPort(port).withInMemoryBackend().build();
    }

    @PostConstruct
    public void postConstruct() {
        api.start();
    }

    @PreDestroy
    public void preDestroy() {
        api.stop();
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:" + port, Regions.DEFAULT_REGION.getName()))
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials())) //use anonymous credentials.
                .build();
        amazonS3.createBucket(defaultBucketName);
        return amazonS3;
    }

}
