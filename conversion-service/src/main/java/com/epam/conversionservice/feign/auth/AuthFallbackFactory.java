package com.epam.conversionservice.feign.auth;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthFallbackFactory implements FallbackFactory<AuthServiceClient> {

    @Override
    public AuthServiceClient create(Throwable throwable) {
        return null;
    }
}
