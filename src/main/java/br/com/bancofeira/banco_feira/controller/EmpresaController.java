package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.CreditosResponseDto;
import br.com.bancofeira.banco_feira.dto.EmpresaCreateDto;
import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.dto.JuntarEmpresaDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.EmpresaService;
import br.com.bancofeira.banco_feira.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final JwtService jwtService;

    public EmpresaController(EmpresaService empresaService, JwtService jwtService) {
        this.empresaService = empresaService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpresaResponseDto>> criarEmpresa(
            @RequestBody @Valid EmpresaCreateDto dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Empresa novaEmpresa = empresaService.criarEmpresa(dto, usuarioLogado);

        String novoToken = jwtService.generateToken(usuarioLogado);

        EmpresaResponseDto empresaDto = EmpresaResponseDto.fromEntity(novaEmpresa);

        ApiResponse<EmpresaResponseDto> response = new ApiResponse<>(
                true,
                "Empresa inscrita com sucesso!",
                empresaDto,
                novoToken
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/evento/{eventoId}/minhas")
    public ResponseEntity<ApiResponse<List<EmpresaResponseDto>>> listarMinhasEmpresasPorEvento(
            @PathVariable Integer eventoId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        List<Empresa> empresas = empresaService.listarMinhasEmpresasPorEvento(eventoId, usuarioLogado);

        List<EmpresaResponseDto> dtos = empresas.stream()
                .map(EmpresaResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas do usuário listadas para o evento.", dtos));
    }

    @GetMapping("/{empresaId}/creditos")
    public ResponseEntity<ApiResponse<CreditosResponseDto>> getCreditosDaMinhaEmpresa(
            @PathVariable Integer empresaId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Empresa empresa = empresaService.getMinhaEmpresa(empresaId, usuarioLogado);

        CreditosResponseDto creditosDto = new CreditosResponseDto(empresa.getCreditos());

        ApiResponse<CreditosResponseDto> response = new ApiResponse<>(true, "Saldo da empresa.", creditosDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/entrar")
    public ResponseEntity<ApiResponse<EmpresaResponseDto>> entrarComoFuncionario(
            @RequestBody @Valid JuntarEmpresaDto dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Empresa empresa = empresaService.adicionarFuncionario(dto, usuarioLogado);

        String novoToken = jwtService.generateToken(usuarioLogado);

        ApiResponse<EmpresaResponseDto> response = new ApiResponse<>(
                true,
                "Você entrou na empresa " + empresa.getNomeFantasia() + " com sucesso!",
                EmpresaResponseDto.fromEntity(empresa),
                novoToken
        );
        return ResponseEntity.ok(response);
    }

}