package com.it.playservice.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfiguration {

    @Bean
    public Mapper mapper() {

        final BeanMappingBuilder builder = new BeanMappingBuilder() {
            @Override
            protected void configure() {

            }
        };

        final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(builder);
        return dozerBeanMapper;
    }
}
