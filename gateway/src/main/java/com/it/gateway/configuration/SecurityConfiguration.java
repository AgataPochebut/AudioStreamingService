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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors()
                .and()
                .authorizeRequests()
                .mvcMatchers("/ui**").permitAll()
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestResolver(new OAuth2AuthorizationRequestResolver() {

                    private OAuth2AuthorizationRequestResolver resolver =
                            new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);

                    @Override
                    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {

                        OAuth2AuthorizationRequest authorizationRequest = resolver.resolve(request);

                        return authorizationRequest != null ?
                                customAuthorizationRequest(authorizationRequest) :
                                null;
                    }

                    @Override
                    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {

                        OAuth2AuthorizationRequest authorizationRequest = resolver.resolve(request, clientRegistrationId);

                        return authorizationRequest != null ?
                                customAuthorizationRequest(authorizationRequest) :
                                null;
                    }

                    private OAuth2AuthorizationRequest customAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {

                        Map<String, Object> additionalParameters =
                                new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
                        additionalParameters.put("access_type", "offline");

                        return OAuth2AuthorizationRequest.from(authorizationRequest)
                                .additionalParameters(additionalParameters)
                                .build();
                    }

                })

                .and()
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
                });

        http
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

