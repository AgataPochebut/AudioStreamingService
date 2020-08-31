package com.epam.songservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class RedisConfiguration {

}
