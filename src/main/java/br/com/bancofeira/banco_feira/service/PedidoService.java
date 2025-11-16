package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.dto.CheckoutRequestDto;
import br.com.bancofeira.banco_feira.dto.ItemCarrinhoDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.*;
import br.com.bancofeira.banco_feira.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final InscricaoRepository inscricaoRepository;
    private final EmpresaRepository empresaRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                         InscricaoRepository inscricaoRepository, EmpresaRepository empresaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.inscricaoRepository = inscricaoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public Pedido realizarPedido(CheckoutRequestDto checkoutDto, Usuario cliente) {
        Inscricao inscricao = inscricaoRepository
                .findByUsuarioIdAndEventoId(cliente.getId(), checkoutDto.getEventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não inscrito neste evento."));

        Empresa empresa = empresaRepository.findById(checkoutDto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada."));

        boolean isFuncionario = cliente.getEmpresas().stream()
                                     .anyMatch(emp -> emp.getId().equals(empresa.getId()));
        
        if (isFuncionario) {
            throw new IllegalStateException("Você não pode comprar produtos da sua própria empresa.");
        }

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemPedido> itensDoPedido = new ArrayList<>();

        Pedido novoPedido = new Pedido();
        novoPedido.setInscricao(inscricao);
        novoPedido.setEmpresa(empresa);
        novoPedido.setStatus(StatusPedido.PENDENTE);

        for (ItemCarrinhoDto itemDto : checkoutDto.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + itemDto.getProdutoId() + " não encontrado."));

            if (!produto.getEmpresa().getId().equals(empresa.getId())) {
                throw new IllegalStateException("Produto " + produto.getNome() + " não pertence à empresa " + empresa.getNomeFantasia());
            }

            if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                throw new IllegalStateException("Estoque insuficiente para " + produto.getNome());
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itemPedido.setPedido(novoPedido);
            itemPedido.setQuantidade(itemDto.getQuantidade());
            itemPedido.setValorUnitario(produto.getValor());

            itensDoPedido.add(itemPedido);

            valorTotal = valorTotal.add(produto.getValor().multiply(BigDecimal.valueOf(itemDto.getQuantidade())));
        }

        if (inscricao.getCreditos().compareTo(valorTotal) < 0) {
            throw new IllegalStateException("Saldo insuficiente. Você tem R$" + inscricao.getCreditos() + ", mas o pedido é R$" + valorTotal);
        }

        inscricao.setCreditos(inscricao.getCreditos().subtract(valorTotal));
        empresa.setCreditos(empresa.getCreditos().add(valorTotal));

        novoPedido.setItens(itensDoPedido);
        novoPedido.setValorTotal(valorTotal);
        novoPedido.setStatus(StatusPedido.PAGO);

        inscricaoRepository.save(inscricao);
        empresaRepository.save(empresa);
        return pedidoRepository.save(novoPedido);
    }

    public List<Pedido> listarPedidosPorEmpresa(Integer empresaId, Usuario vendedorLogado) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada."));

        boolean temPermissao = vendedorLogado.getEmpresas().stream()
                .anyMatch(emp -> emp.getId().equals(empresa.getId()));

        if (!temPermissao) {
            throw new IllegalStateException("Acesso negado. Você não tem permissão para ver os pedidos desta empresa.");
        }

        return pedidoRepository.findByEmpresaIdOrderByDataPedidoDesc(empresaId);
    }

    @Transactional
    public Pedido marcarPedidoComoEntregue(Integer pedidoId, Usuario vendedorLogado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado."));

        validarPermissao(pedido.getEmpresa(), vendedorLogado);

        if (pedido.getStatus() != StatusPedido.PAGO) {
            throw new IllegalStateException("Este pedido não pode ser marcado como entregue (Status: " + pedido.getStatus() + ")");
        }

        pedido.setStatus(StatusPedido.ENTREGUE);
        return pedidoRepository.save(pedido);
    }

    private void validarPermissao(Empresa empresa, Usuario usuarioLogado) {
        boolean temPermissao = usuarioLogado.getEmpresas().stream()
                .anyMatch(emp -> emp.getId().equals(empresa.getId()));

        if (!temPermissao) {
            throw new IllegalStateException("Acesso negado. Você não tem permissão para gerenciar esta empresa.");
        }
    }

    public List<Pedido> listarPedidosPorCliente(Usuario clienteLogado) {
        return pedidoRepository.findByInscricaoUsuarioIdOrderByDataPedidoDesc(clienteLogado.getId());
    }
}