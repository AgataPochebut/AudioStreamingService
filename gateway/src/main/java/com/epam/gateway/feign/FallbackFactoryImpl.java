package com.epam.gateway.feign;

import feign.hystrix.FallbackFactory;

public class FallbackFactoryImpl implements FallbackFactory {
    @Override
    public Object create(Throwable throwable) {
        return throwable;
    }
}
