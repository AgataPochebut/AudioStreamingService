package com.epam.authservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private AccessDeniedHandler accessDeniedHandler;
//    @Autowired
//    private AuthenticationEntryPoint authenticationEntryPoint;
//    @Autowired
//    private AuthenticationSuccessHandler authenticationSuccessHandler;
//    @Autowired
//    private AuthenticationFailureHandler authenticationFailureHandler;

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
//                .loginPage("/auth/login") //не надо, работает само
//                .userInfoEndpoint()
//                .userService(oAuth2UserService)
//                .customUserType(OAuth2User.class, "google")

                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        String message = authentication.getDetails().toString();

                        log.debug(message);

                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), message);
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        String errorMessage = e.getMessage();

                        log.error(errorMessage, e);

                        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
                    }
                })
        ;
    }
}

