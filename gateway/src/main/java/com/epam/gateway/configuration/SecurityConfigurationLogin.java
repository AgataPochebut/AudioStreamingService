package com.epam.gateway.configuration;

import com.epam.gateway.feign.AuthServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
@Slf4j
public class SecurityConfigurationLogin extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
//                .csrf().disable().cors()
//
//                .and()
                .authorizeRequests()
//                .mvcMatchers("/**").permitAll()
//                .antMatchers("/api/v2/api-docs","/v2/api-docs","/api/swagger-resources/**", "/swagger-resources/**", "/api/swagger-ui.html**", "/api/webjars/**").permitAll()
//                .antMatchers("/auth/**").permitAll()
//                .antMatchers("/songs/**").permitAll()
                .anyRequest().authenticated()

//                .and()
//                .exceptionHandling()
//                .accessDeniedHandler(new AccessDeniedHandler() {
//                    @Override
//                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
//                        String errorMessage = e.getMessage();
//
//                        log.error(errorMessage, e);
//
//                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
//                    }
//                })//check roles
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    @Override
//                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//
//                        String errorMessage = e.getMessage();
//
//                        log.error(errorMessage, e);
//
//                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
//                    }
//                }) //check autorization //либо authenticationEntryPoint либо loginPage

                .and()
                .oauth2Login()

//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                        String message = authentication.getDetails().toString();
//                        log.debug(message);
//                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), message);
//
//                        if (authentication.getPrincipal() instanceof OidcUser) {
//                            OidcUser user = (OidcUser) authentication.getPrincipal();
//
//                            User entity = authServiceClient.getUserByAccount(user.getEmail()).getBody();
//                            if (entity == null) {
//                                entity = User.builder()
//                                        .account(user.getEmail())
//                                        .roles(user.getAuthorities().stream()
//                                                .map((i) -> Role.builder().name(((GrantedAuthority) i).getAuthority()).build())
//                                                .collect(Collectors.toSet()))
//                                        .name(user.getFullName())
//                                        .build();
//                                authServiceClient.createUser(entity);
//                            } else {
//                                entity.setName(user.getFullName());
//                                authServiceClient.updateUser(entity.getId(), entity);
//                            }
//                        }
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//                        String errorMessage = e.getMessage();
//                        log.error(errorMessage, e);
//                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
//                    }
//                })
        ;
    }
}

