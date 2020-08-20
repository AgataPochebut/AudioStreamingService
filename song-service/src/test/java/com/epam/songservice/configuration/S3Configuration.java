package com.epam.songservice.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.SocketUtils;

@TestConfiguration
public class S3Configuration {

    @Value("${s3.defaultBucket}")
    private String defaultBucketName;

    @Bean
    public AmazonS3 amazonS3Client() {
        int port = SocketUtils.findAvailableTcpPort();
        S3Mock api = new S3Mock.Builder().withPort(port).withInMemoryBackend().build();
        api.start(); // Start the Mock S3 server locally on available port

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:" + port, Regions.DEFAULT_REGION.getName()))
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials())) //use anonymous credentials.
                .build();
        amazonS3.createBucket(defaultBucketName);
        return amazonS3;
    }

}
