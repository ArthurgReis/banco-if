package br.com.bancofeira.banco_feira.repository;

import br.com.bancofeira.banco_feira.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByEmpresaIdOrderByDataPedidoDesc(Integer empresaId);
    List<Pedido> findByInscricaoUsuarioIdOrderByDataPedidoDesc(Integer usuarioId);
}