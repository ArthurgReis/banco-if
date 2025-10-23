package br.com.bancofeira.banco_feira.repository;

import br.com.bancofeira.banco_feira.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findByEmpresaId(Integer empresaId);
}