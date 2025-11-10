package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.ProdutoResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Produto;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas/{empresaId}/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProdutoResponseDto>> criarProduto(
            @PathVariable Integer empresaId,
            @RequestBody @Valid Produto produto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Produto novoProduto = produtoService.criarProduto(produto, empresaId, usuarioLogado);
        ApiResponse<ProdutoResponseDto> response = new ApiResponse<>(true, "Produto cadastrado!", ProdutoResponseDto.fromEntity(novoProduto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProdutoResponseDto>>> listarProdutos(
            @PathVariable Integer empresaId) {

        List<Produto> produtosEntidades = produtoService.listarProdutosPorEmpresa(empresaId);
        List<ProdutoResponseDto> produtosDto = produtosEntidades.stream()
                .map(ProdutoResponseDto::fromEntity)
                .collect(Collectors.toList());
        ApiResponse<List<ProdutoResponseDto>> response = new ApiResponse<>(true, "Produtos listados.", produtosDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{produtoId}")
    public ResponseEntity<ApiResponse<ProdutoResponseDto>> atualizarProduto(
            @PathVariable Integer empresaId,
            @PathVariable Integer produtoId,
            @RequestBody @Valid Produto produto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Produto produtoAtualizado = produtoService.atualizarProduto(empresaId, produtoId, produto, usuarioLogado);
        ApiResponse<ProdutoResponseDto> response = new ApiResponse<>(true, "Produto atualizado!", ProdutoResponseDto.fromEntity(produtoAtualizado));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<ApiResponse<Void>> deletarProduto(
            @PathVariable Integer empresaId,
            @PathVariable Integer produtoId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        produtoService.deletarProduto(empresaId, produtoId, usuarioLogado);
        ApiResponse<Void> response = new ApiResponse<>(true, "Produto deletado!", null);
        return ResponseEntity.ok(response);
    }
}