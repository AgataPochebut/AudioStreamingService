package com.epam.gateway.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
@Slf4j
public class SecurityConfigurationClient extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
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
                .oauth2Client()
                .and()
                .formLogin()
        ;
    }
}

