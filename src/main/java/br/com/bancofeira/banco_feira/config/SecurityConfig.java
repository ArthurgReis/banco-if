package br.com.bancofeira.banco_feira.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider,
                                                   JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // --- ROTAS PÚBLICAS (NÃO PRECISA DE LOGIN) ---
                        .requestMatchers("/api/auth/**").permitAll() // Login, Esqueci Senha
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Criar novo usuário
                        .requestMatchers(HttpMethod.GET, "/api/empresas/*/produtos").permitAll() // Ver produtos
                        // ... (adicionar rotas públicas de 'ver eventos' se necessário) ...

                        // --- ROTAS DE ADMIN (PRECISA DE ROLE_ADMIN) ---
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/eventos/**").hasRole("ADMIN") // Admin gerencia eventos
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("ADMIN") // Admin vê usuários

                        // --- ROTAS AUTENTICADAS (PRECISA ESTAR LOGADO, QUALQUER PAPEL) ---
                        .requestMatchers("/api/inscricoes/**").authenticated() // Inscrever-se (cliente ou empresa)
                        .requestMatchers("/api/empresas/**").authenticated() // Criar/gerenciar empresa e produtos
                        .requestMatchers("/api/usuarios/me").authenticated() // Ver seus próprios dados

                        .anyRequest().authenticated() // Qualquer outra rota não listada exige login
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}