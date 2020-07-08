package com.epam.songservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //сессий нет
                .csrf().disable().cors()

                .and()
                .authorizeRequests()
                .mvcMatchers("/**").permitAll()
                .antMatchers("/index","/hystrix/**","/actuator/**").permitAll()
                .antMatchers("/api/v2/api-docs","/v2/api-docs","/api/swagger-resources/**", "/swagger-resources/**", "/api/swagger-ui.html**", "/api/webjars/**").permitAll()
                .anyRequest().authenticated()

//                .and()
//                .exceptionHandling()
//                .accessDeniedHandler(new AccessDeniedHandler() {
//                    @Override
//                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
//                        String errorMessage = e.getMessage();
//                        log.error(errorMessage, e);
//                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
//                    }
//                })//check roles
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    @Override
//                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//                        String errorMessage = e.getMessage();
//                        log.error(errorMessage, e);
//                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
//                    }
//                }) //check autorization //либо authenticationEntryPoint либо loginPage

                .and()
                .oauth2ResourceServer()
                .opaqueToken()
                .introspector(new OpaqueTokenIntrospector() {
                    @Override
                    public OAuth2AuthenticatedPrincipal introspect(String s) {
                        RestTemplate restTemplate = new RestTemplate();

                        String url = UriComponentsBuilder
                                .fromHttpUrl("https://www.googleapis.com/oauth2/v3/userinfo")
                                .queryParam("access_token", s)
                                .toUriString();

                        HttpHeaders headers = new HttpHeaders();
//                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                        HttpEntity<?> request = new HttpEntity<>(headers);

                        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

                        Map<String, Object> attributes = (Map)response.getBody();

                        Set<GrantedAuthority> authorities = new LinkedHashSet();
                        authorities.add(new OAuth2UserAuthority(attributes));

                        //token info
//                        OAuth2AccessToken token = userRequest.getAccessToken();
//                        Iterator var8 = token.getScopes().iterator();
//
//                        while(var8.hasNext()) {
//                            String authority = (String)var8.next();
//                            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
//                        }

                        return new DefaultOAuth2User(authorities, attributes, "name");
                    }
                })
        ;
    }
}

