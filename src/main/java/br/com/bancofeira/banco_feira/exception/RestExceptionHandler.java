package br.com.bancofeira.banco_feira.exception;

import br.com.bancofeira.banco_feira.model.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @SuppressWarnings("null") MethodArgumentNotValidException ex, @SuppressWarnings("null") HttpHeaders headers, @SuppressWarnings("null") HttpStatusCode status, @SuppressWarnings("null") WebRequest request) {

        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> "O campo '" + fieldError.getField() + "' " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<List<String>> response = new ApiResponse<>(false, "Dados inválidos. Verifique os erros.", errorMessages);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ApiResponse<Object>> handleDataIntegrity(DataIntegrityViolationException e) {
        String mensagem = "Erro de integridade dos dados. Provavelmente um e-mail ou CPF já cadastrado.";
        ApiResponse<Object> response = new ApiResponse<>(false, mensagem, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    private ResponseEntity<ApiResponse<Object>> handleIllegalStateException(IllegalStateException e) {
        ApiResponse<Object> response = new ApiResponse<>(false, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException e) {
        ApiResponse<Object> response = new ApiResponse<>(false, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception e) {

        e.printStackTrace();
        ApiResponse<Object> response = new ApiResponse<>(false, "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Object>> handleDisabledException(DisabledException e) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Conta não ativada. Por favor, verifique seu e-mail para o link de confirmação.",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException e) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "E-mail ou senha inválidos.",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}