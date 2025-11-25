package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.AuthResponseDto;
import br.com.bancofeira.banco_feira.dto.EsqueciSenhaDto;
import br.com.bancofeira.banco_feira.dto.LoginRequestDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/esqueci-senha")
    public ResponseEntity<ApiResponse<Void>> esqueciSenha(@RequestBody @Valid EsqueciSenhaDto esqueciSenhaDto) {
        authService.esqueciSenha(esqueciSenhaDto.getEmail());
        ApiResponse<Void> response = new ApiResponse<>(true, "Se o e-mail estiver cadastrado, a senha foi redefinida para o seu CPF.", null);
        return ResponseEntity.ok(response);
    }


}