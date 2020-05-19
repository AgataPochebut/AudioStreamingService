//package com.epam.gateway.configuration;
//
//import com.epam.gateway.converter.UserConverter;
//import com.epam.gateway.model.Role;
//import com.epam.gateway.model.User;
//import org.dozer.CustomConverter;
//import org.dozer.DozerBeanMapper;
//import org.dozer.Mapper;
//import org.dozer.loader.api.BeanMappingBuilder;
//import org.dozer.loader.api.FieldsMappingOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Configuration
//public class MappingConfiguration {
//
//    @Bean
//    public Mapper mapper() {
//
//        final BeanMappingBuilder builder = new BeanMappingBuilder() {
//            @Override
//            protected void configure() {
//
//                mapping(OAuth2User.class, User.class)
//                        .fields("client", "client", FieldsMappingOptions.customConverter(ClientConverter.class))
//                        .fields("service", "service", FieldsMappingOptions.customConverter(ServiceConverter.class))
//                        .fields("resources", "resources", FieldsMappingOptions.customConverter(ResourceConverter.class));
//
//                mapping(GrantedAuthority.class, Role.class)
//                        .fields("name", "name")
//                        .fields("user", "user", FieldsMappingOptions.customConverter(UserConverter.class));
//
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
//
//        final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
//        dozerBeanMapper.addMapping(builder);
//        return dozerBeanMapper;
//    }
//
//    public class RoleConverter implements CustomConverter {
//
//        @Override
//        public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {
//
//            if (source == null)
//                return null;
//
//            else if (source instanceof Set) {
//                return ((Set)source).stream()
//                        .map((i) -> roleService.findById((Long) i))
//                        .collect(Collectors.toSet());
//            }
//
//            else return null;
//        }
//    }
//
//    public class UserConverter implements CustomConverter {
//
//        @Override
//        public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {
//
//            if (source == null)
//                return null;
//
//            else if (source instanceof Long) {
//                return userService.findById((Long) source);
//            }
//
//            else return null;
//        }
//    }
//
//
//}
