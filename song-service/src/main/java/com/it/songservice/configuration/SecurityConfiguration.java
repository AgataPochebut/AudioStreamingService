package com.it.songservice.configuration;

import com.it.commonservice.model.auth.AuthUser;
import com.it.songservice.feign.auth.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .opaqueToken()
                .introspector(token -> {
                    ResponseEntity response = authServiceClient.getUser(token);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        AuthUser user = (AuthUser) response.getBody();
                        return user;
                    }
                    Throwable throwable = (Throwable) response.getBody();
                    throw new AuthenticationServiceException(throwable.getCause().getMessage(), throwable);
                })
        ;
    }
}

