package br.com.bancofeira.banco_feira.repository;
import br.com.bancofeira.banco_feira.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
}