package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.dto.AuthResponseDto;
import br.com.bancofeira.banco_feira.dto.LoginRequestDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.ConfirmationToken;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.ConfirmationTokenRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository tokenRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder, ConfirmationTokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public AuthResponseDto login(LoginRequestDto request) {
 
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(usuario);
        
        return new AuthResponseDto(token);
    }

    @Transactional
    public void esqueciSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com este e-mail."));

        String cpf = usuario.getCpf();
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalStateException("Usuário não possui CPF cadastrado para redefinição.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(cpf);

        usuario.setSenha(novaSenhaCriptografada);
        usuarioRepository.save(usuario);

    }

    @Transactional
    public void confirmarConta(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de confirmação inválido ou não encontrado."));

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado.");
        }

        Usuario usuario = confirmationToken.getUsuario();
        usuario.setEnabled(true);
        usuarioRepository.save(usuario);

        tokenRepository.delete(confirmationToken);
    }
}