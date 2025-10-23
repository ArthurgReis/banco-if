package br.com.bancofeira.banco_feira.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.ProdutoResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Produto;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.ProdutoService;
import jakarta.validation.Valid;

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
        ProdutoResponseDto produtoDto = ProdutoResponseDto.fromEntity(novoProduto);
        ApiResponse<ProdutoResponseDto> response = new ApiResponse<>(true, "Produto cadastrado com sucesso!", produtoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProdutoResponseDto>>> listarProdutos(
            @PathVariable Integer empresaId) {
            
        List<Produto> produtosEntidades = produtoService.listarProdutosPorEmpresa(empresaId);
        List<ProdutoResponseDto> produtosDto = produtosEntidades.stream()
                .map(ProdutoResponseDto::fromEntity)
                .collect(Collectors.toList());
        ApiResponse<List<ProdutoResponseDto>> response = new ApiResponse<>(true, "Produtos listados com sucesso!", produtosDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{produtoId}")
    public ResponseEntity<ApiResponse<ProdutoResponseDto>> atualizarProduto(
            @PathVariable Integer empresaId,
            @PathVariable Integer produtoId,
            @RequestBody @Valid Produto produto, 
            @AuthenticationPrincipal Usuario usuarioLogado) {
            
        Produto produtoAtualizado = produtoService.atualizarProduto(empresaId, produtoId, produto, usuarioLogado);
        ProdutoResponseDto produtoDto = ProdutoResponseDto.fromEntity(produtoAtualizado);
        ApiResponse<ProdutoResponseDto> response = new ApiResponse<>(true, "Produto atualizado com sucesso!", produtoDto);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{produtoId}")
    public ResponseEntity<ApiResponse<Void>> deletarProduto(
            @PathVariable Integer empresaId,
            @PathVariable Integer produtoId,
            @AuthenticationPrincipal Usuario usuarioLogado) {
            
        produtoService.deletarProduto(empresaId, produtoId, usuarioLogado);
        ApiResponse<Void> response = new ApiResponse<>(true, "Produto deletado com sucesso!", null);
        return ResponseEntity.ok(response);
    }
}