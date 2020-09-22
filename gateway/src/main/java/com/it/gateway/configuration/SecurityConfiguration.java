package com.it.gateway.configuration;

import com.it.commonservice.model.auth.AuthUser;
import com.it.gateway.feign.auth.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthServiceClient authServiceClient;

    //no token->bearertokenfilter->LoginUrlAuthenticationEntryPoint->OidcServise->AuthService->if error->try 5-6 times
    //token->bearertokenfilter->interceptor->if error->BearerTokenAuthenticationEntryPoint
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors()
                .and()
                .authorizeRequests()
//                .mvcMatchers("/").permitAll()
                .mvcMatchers("/ui**").permitAll()
                .anyRequest().authenticated()
                //no token
                .and()
                .oauth2Login()
                //token
                .and()
                .oauth2ResourceServer()
                .opaqueToken()
                .introspector(new OpaqueTokenIntrospector() {
                    @Override
                    public OAuth2AuthenticatedPrincipal introspect(String s) {
                        ResponseEntity<?> response = authServiceClient.getUser(s);
//                        if(!response.getStatusCode().is2xxSuccessful()) {
//                            throw new Exception("Login");
//                        }
                        AuthUser user = (AuthUser) response.getBody();
                        return new DefaultOAuth2User(user.getAuthorities(), user.getAttributes(), "name");
                    }
                })
        ;
    }
}

