package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.EmpresaCreateDto;
import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpresaResponseDto>> criarEmpresa(
            @RequestBody @Valid EmpresaCreateDto dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Empresa novaEmpresa = empresaService.criarEmpresa(dto, usuarioLogado);
        ApiResponse<EmpresaResponseDto> response = new ApiResponse<>(true, "Empresa criada com sucesso!", EmpresaResponseDto.fromEntity(novaEmpresa));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}