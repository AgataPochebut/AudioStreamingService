package com.it.songservice.feign.conversion;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ConversionFallbackFactory implements FallbackFactory<ConversionClient> {

    @Override
    public ConversionClient create(Throwable throwable) {
        return new ConversionFallback(throwable);
    }
}
