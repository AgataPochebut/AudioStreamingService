package com.epam.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .cors()

                .and()
                .authorizeRequests()
                .antMatchers("/api/v2/api-docs","/v2/api-docs","/api/swagger-resources/**", "/swagger-resources/**", "/api/swagger-ui.html**", "/api/webjars/**").permitAll()

//                .antMatchers("/auth/**").permitAll()
//                .antMatchers(HttpMethod.POST,"/users/**").permitAll()
//                .antMatchers("/events/**").authenticated()
//                .antMatchers(HttpMethod.POST).hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT).hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
//                .anyRequest().authenticated()

                //check roles
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)

                //check autorization
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)

//                .and()
//                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                ;
    }

}
