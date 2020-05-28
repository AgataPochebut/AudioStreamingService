package com.epam.indexservice.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfiguration {

    @Bean
    public Mapper mapper() {

//        final BeanMappingBuilder builder = new BeanMappingBuilder() {
//            @Override
//            protected void configure() {
//
////                mapping(EventRequestDto.class, Event.class)
////                        .fields("client", "client", FieldsMappingOptions.customConverter(ClientConverter.class))
////                        .fields("service", "service", FieldsMappingOptions.customConverter(ServiceConverter.class))
////                        .fields("resources", "resources", FieldsMappingOptions.customConverter(ResourceConverter.class));
////
////                mapping(ClientRequestDto.class, Client.class)
////                        .fields("name", "name")
////                        .fields("user", "user", FieldsMappingOptions.customConverter(UserConverter.class));
////
////                mapping(UserRequestDto.class, User.class)
////                        .fields("name", "name")
////                        .fields("password", "password")
////                        .fields("roles", "roles", FieldsMappingOptions.customConverter(RoleConverter.class));
////
////                mapping(Session.class, SessionResponseDTO.class)
////                        .fields("accessToken.key", "accessToken")
////                        .fields("refreshToken.key", "refreshToken");
//
//            }
//        };

        final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
//        dozerBeanMapper.addMapping(builder);
        return dozerBeanMapper;
    }
}
