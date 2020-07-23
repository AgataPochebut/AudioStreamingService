package com.epam.gateway.configuration;

import com.epam.commonservice.model.auth.AuthUser;
import com.epam.gateway.feign.auth.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

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
                .mvcMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                //check autorization //либо authenticationEntryPoint либо loginPage //when introspector failed, else login
                .defaultAuthenticationEntryPointFor(
                        new BearerTokenAuthenticationEntryPoint(),
                        new RequestHeaderRequestMatcher(HttpHeaders.AUTHORIZATION))
                .and()
                .oauth2Login()
                .and()
                .oauth2ResourceServer()
                .opaqueToken()
                .introspector(new OpaqueTokenIntrospector() {
                    @Override
                    public OAuth2AuthenticatedPrincipal introspect(String s) {
                        AuthUser user = (AuthUser) authServiceClient.getUser(s).getBody();
                        return new DefaultOAuth2User(user.getAuthorities(), user.getAttributes(), "name");
                    }
                })
        ;
    }
}

