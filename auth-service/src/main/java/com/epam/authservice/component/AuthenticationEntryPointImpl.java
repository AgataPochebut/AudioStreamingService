package com.epam.authservice.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * handle exc when no authentication
 */
@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        String errorMessage = e.getMessage();
        errorMessage="AuthenticationEntryPoint";
        log.error(errorMessage, e);
//
//        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.setContentType("application/json");
//        new ObjectMapper().writeValue(response.getWriter(), errorResponseDTO);

        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorMessage);
    }
}
