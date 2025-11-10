package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Produto;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EmpresaRepository empresaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, EmpresaRepository empresaRepository) {
        this.produtoRepository = produtoRepository;
        this.empresaRepository = empresaRepository;
    }

    /**
     * Valida se o usuário logado tem permissão para gerenciar a empresa alvo.
     */
    private void validarPermissao(Empresa empresa, Usuario usuarioLogado) {
        boolean temPermissao = usuarioLogado.getEmpresas().stream()
                .anyMatch(emp -> emp.getId().equals(empresa.getId()));

        if (!temPermissao) {
            throw new IllegalStateException("Acesso negado. Você não tem permissão para gerenciar esta empresa.");
        }
    }

    @Transactional
    public Produto criarProduto(Produto produto, Integer empresaId, Usuario usuarioLogado) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com o ID: " + empresaId));

        validarPermissao(empresa, usuarioLogado);

        produto.setEmpresa(empresa);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutosPorEmpresa(Integer empresaId) {
        if (!empresaRepository.existsById(empresaId)) {
            throw new ResourceNotFoundException("Empresa não encontrada com o ID: " + empresaId);
        }
        return produtoRepository.findByEmpresaId(empresaId);
    }

    @Transactional
    public Produto atualizarProduto(Integer empresaId, Integer produtoId, Produto dadosProduto, Usuario usuarioLogado) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com o ID: " + empresaId));

        validarPermissao(empresa, usuarioLogado);

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoId));

        if (!produto.getEmpresa().getId().equals(empresa.getId())) {
            throw new IllegalStateException("Conflito: Este produto não pertence à empresa informada.");
        }

        produto.setNome(dadosProduto.getNome());
        produto.setValor(dadosProduto.getValor());
        produto.setQuantidadeEstoque(dadosProduto.getQuantidadeEstoque());

        return produtoRepository.save(produto);
    }

    @Transactional
    public void deletarProduto(Integer empresaId, Integer produtoId, Usuario usuarioLogado) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com o ID: " + empresaId));

        validarPermissao(empresa, usuarioLogado);

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoId));

        if (!produto.getEmpresa().getId().equals(empresa.getId())) {
            throw new IllegalStateException("Conflito: Este produto não pertence à empresa informada.");
        }

        produtoRepository.delete(produto);
    }
}