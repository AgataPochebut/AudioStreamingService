package com.it.searchservice.configuration;

import com.it.commonservice.model.auth.AuthUser;
import com.it.searchservice.feign.auth.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;

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
                .exceptionHandling()
                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) //check auth //when introspector failed
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

