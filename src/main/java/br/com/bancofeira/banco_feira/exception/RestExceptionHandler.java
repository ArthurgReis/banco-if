package br.com.bancofeira.banco_feira.exception;

import java.util.HashMap;
import java.util.Map;

import br.com.bancofeira.banco_feira.model.ApiResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

   
    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ApiResponse<Object>> handleDataIntegrity(DataIntegrityViolationException e) {
        ApiResponse<Object> response = new ApiResponse<>(false, "Erro de integridade dos dados. Provavelmente um e-mail ou CPF já cadastrado.", null);
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
}