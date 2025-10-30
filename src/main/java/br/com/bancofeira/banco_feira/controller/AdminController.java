package br.com.bancofeira.banco_feira.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.dto.UsuarioResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.AdminService;

@RestController
@RequestMapping("/api/admin") 
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<ApiResponse<List<UsuarioResponseDto>>> listarUsuarios() {
        List<Usuario> usuarios = adminService.listarTodosUsuarios();
        
        List<UsuarioResponseDto> dtos = usuarios.stream()
                .map(UsuarioResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuários listados com sucesso.", dtos));
    }

    @GetMapping("/empresas")
    public ResponseEntity<ApiResponse<List<EmpresaResponseDto>>> listarEmpresas() {
        List<Empresa> empresas = adminService.listarTodasEmpresas();

        List<EmpresaResponseDto> dtos = empresas.stream()
                .map(EmpresaResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas listadas com sucesso.", dtos));
    }

}