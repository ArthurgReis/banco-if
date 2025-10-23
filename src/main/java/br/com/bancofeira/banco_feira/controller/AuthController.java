package br.com.bancofeira.banco_feira.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.AuthResponseDto;
import br.com.bancofeira.banco_feira.dto.LoginRequestDto;
import br.com.bancofeira.banco_feira.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth") 
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}