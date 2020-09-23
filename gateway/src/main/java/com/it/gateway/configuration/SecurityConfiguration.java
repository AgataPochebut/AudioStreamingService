package com.it.gateway.configuration;

import com.it.commonservice.model.auth.AuthUser;
import com.it.gateway.feign.auth.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

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
//                .mvcMatchers("/").permitAll()
                .mvcMatchers("/ui**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(userRequest -> {
                    OAuth2AccessToken accessToken = userRequest.getAccessToken();
                    String token = accessToken.getTokenValue();
                    ResponseEntity response = authServiceClient.getUser(token);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        AuthUser user = (AuthUser) response.getBody();
                        return new DefaultOidcUser(user.getAuthorities(), userRequest.getIdToken(), new OidcUserInfo(user.getAttributes()), "name");
                    }
                    Throwable throwable = (Throwable) response.getBody();
                    throw new AuthenticationServiceException(throwable.getCause().getMessage(), throwable);
                })
        ;
    }
}

