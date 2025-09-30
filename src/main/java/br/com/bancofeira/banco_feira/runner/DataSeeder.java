package br.com.bancofeira.banco_feira.runner;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Executando o Data Seeder...");
        createRolesIfNeeded();
        createAdminIfNeeded();
        System.out.println("Data Seeder finalizado.");
    }

    private void createRolesIfNeeded() {
        if (roleRepository.findByNome("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
            System.out.println("Role ADMIN criada.");
        }
        if (roleRepository.findByNome("ROLE_EMPRESA").isEmpty()) {
            roleRepository.save(new Role("ROLE_EMPRESA"));
            System.out.println("Role EMPRESA criada.");
        }
        if (roleRepository.findByNome("ROLE_CLIENTE").isEmpty()) {
            roleRepository.save(new Role("ROLE_CLIENTE"));
            System.out.println("Role CLIENTE criada.");
        }
    }

    private void createAdminIfNeeded() {
        if (usuarioRepository.findByEmail("admin@bancofeira.com").isEmpty()) {
            Role adminRole = roleRepository.findByNome("ROLE_ADMIN").get();
            Role clientRole = roleRepository.findByNome("ROLE_CLIENTE").get();

            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@bancofeira.com");
            admin.setCpf("00000000000"); 
            admin.setSenha(passwordEncoder.encode("admin123")); 
            admin.setCreditos(BigDecimal.valueOf(999999));
            admin.setRoles(Set.of(adminRole, clientRole)); 

            usuarioRepository.save(admin);
            System.out.println("Usuário ADMIN criado.");
        }
    }
}