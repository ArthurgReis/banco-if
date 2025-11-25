package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.CheckoutRequestDto;
import br.com.bancofeira.banco_feira.dto.CreditosResponseDto;
import br.com.bancofeira.banco_feira.dto.EmpresaCreateDto;
import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.dto.EmpresaUpdateDto;
import br.com.bancofeira.banco_feira.dto.JuntarEmpresaDto;
import br.com.bancofeira.banco_feira.dto.PedidoResponseDto;
import br.com.bancofeira.banco_feira.dto.VendaSimulacaoResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Cliente;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Pedido;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.ClienteRepository;
import br.com.bancofeira.banco_feira.service.ClienteService;
import br.com.bancofeira.banco_feira.service.EmpresaService;
import br.com.bancofeira.banco_feira.service.JwtService;
import br.com.bancofeira.banco_feira.service.PedidoService;
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
    private final ClienteService clienteService;
    private final JwtService jwtService;
    private final PedidoService pedidoService;
    private final ClienteRepository clienteRepository;

    public EmpresaController(EmpresaService empresaService, JwtService jwtService, ClienteService clienteService, PedidoService pedidoService, ClienteRepository clienteRepository) {
        this.empresaService = empresaService;
        this.clienteService = clienteService;
        this.jwtService = jwtService;
        this.pedidoService = pedidoService;
        this.clienteRepository = clienteRepository;
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaResponseDto>> atualizarEmpresa(
            @PathVariable Integer id,
            @RequestBody @Valid EmpresaUpdateDto dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Empresa empresaAtualizada = empresaService.atualizarEmpresa(id, dto, usuarioLogado);
        
        ApiResponse<EmpresaResponseDto> response = new ApiResponse<>(
            true, 
            "Empresa atualizada com sucesso!", 
            EmpresaResponseDto.fromEntity(empresaAtualizada)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarEmpresa(
            @PathVariable Integer id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        empresaService.deletarEmpresa(id, usuarioLogado);
        
        ApiResponse<Void> response = new ApiResponse<>(
            true, 
            "Empresa deletada com sucesso!", 
            null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/venda/simulacao")
        public ResponseEntity<ApiResponse<VendaSimulacaoResponseDto>> simularVenda(
                @RequestBody @Valid CheckoutRequestDto checkout) {
            
            VendaSimulacaoResponseDto simulacao = pedidoService.simularVenda(checkout);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Cálculo realizado.", simulacao));
        }

        @PostMapping("/venda")
        public ResponseEntity<ApiResponse<PedidoResponseDto>> realizarVenda(
                @AuthenticationPrincipal Usuario usuarioLogado,
                @RequestBody @Valid CheckoutRequestDto checkout) {

            Pedido pedido = pedidoService.processarVenda(checkout, usuarioLogado);
            
            return ResponseEntity.ok(new ApiResponse<>(
                true, 
                "Venda realizada com sucesso!", 
                PedidoResponseDto.fromEntity(pedido)
            ));
        }
}