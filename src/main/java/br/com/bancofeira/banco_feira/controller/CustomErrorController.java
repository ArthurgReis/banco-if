package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.model.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ApiResponse<Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        String mensagem = "Ocorreu um erro inesperado.";
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                httpStatus = HttpStatus.NOT_FOUND;
                mensagem = "Recurso não encontrado. Verifique a URL e tente novamente.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                httpStatus = HttpStatus.FORBIDDEN;
                mensagem = "Acesso negado.";
            }
        }

        ApiResponse<Object> response = new ApiResponse<>(false, mensagem, null);
        return new ResponseEntity<>(response, httpStatus);
    }
}