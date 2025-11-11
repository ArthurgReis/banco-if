package br.com.bancofeira.banco_feira.exception;

import br.com.bancofeira.banco_feira.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                false,
                "Acesso negado. Você não tem permissão para acessar este recurso.",
                null
        );

        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}