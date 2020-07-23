package com.epam.authservice.configuration;

import com.epam.authservice.feign.auth.AuthServiceClient;
import com.epam.commonservice.model.auth.AuthUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthServiceClient authServiceClient;

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors()
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        String errorMessage = e.getMessage();
                        log.error(errorMessage, e);
                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
                    }
                })//check roles
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        String errorMessage = e.getMessage();
                        log.error(errorMessage, e);
                        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
                    }
                }) //check autorization //when introspector failed
                .and()
                .oauth2ResourceServer()
                .opaqueToken()
                .introspector(new OpaqueTokenIntrospector() {
                    @Override
                    public OAuth2AuthenticatedPrincipal introspect(String s) {
                        AuthUser user = (AuthUser) authServiceClient.getUser(s).getBody();
                        return user;
                    }
                })
        ;
    }
}

