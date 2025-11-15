package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.CheckoutRequestDto;
import br.com.bancofeira.banco_feira.dto.PedidoResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Pedido;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<PedidoResponseDto>> realizarPedido(
            @RequestBody @Valid CheckoutRequestDto checkoutDto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Pedido novoPedido = pedidoService.realizarPedido(checkoutDto, usuarioLogado);
        PedidoResponseDto dto = PedidoResponseDto.fromEntity(novoPedido);
        ApiResponse<PedidoResponseDto> response = new ApiResponse<>(true, "Pedido realizado e pago com sucesso!", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<ApiResponse<List<PedidoResponseDto>>> listarPedidosDaEmpresa(
            @PathVariable Integer empresaId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        List<Pedido> pedidos = pedidoService.listarPedidosPorEmpresa(empresaId, usuarioLogado);
        List<PedidoResponseDto> dtos = pedidos.stream()
                .map(PedidoResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Pedidos da empresa listados.", dtos));
    }

    @PatchMapping("/{pedidoId}/entregar")
    public ResponseEntity<ApiResponse<PedidoResponseDto>> marcarComoEntregue(
            @PathVariable Integer pedidoId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Pedido pedido = pedidoService.marcarPedidoComoEntregue(pedidoId, usuarioLogado);
        PedidoResponseDto dto = PedidoResponseDto.fromEntity(pedido);

        return ResponseEntity.ok(new ApiResponse<>(true, "Pedido marcado como entregue.", dto));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<PedidoResponseDto>>> listarMeusPedidos(
            @AuthenticationPrincipal Usuario usuarioLogado) {

        List<Pedido> pedidos = pedidoService.listarPedidosPorCliente(usuarioLogado);
        List<PedidoResponseDto> dtos = pedidos.stream()
                .map(PedidoResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Meus pedidos listados.", dtos));
    }

}