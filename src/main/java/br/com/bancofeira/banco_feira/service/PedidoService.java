package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.dto.CheckoutRequestDto;
import br.com.bancofeira.banco_feira.dto.ItemCarrinhoDto;
import br.com.bancofeira.banco_feira.dto.VendaSimulacaoResponseDto;
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
    private final EmpresaRepository empresaRepository;
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                         EmpresaRepository empresaRepository, ClienteRepository clienteRepository, ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.empresaRepository = empresaRepository;
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
    }

    public VendaSimulacaoResponseDto simularVenda(CheckoutRequestDto dto) {
        Cliente cliente = clienteService.buscarOuCriar(dto.getCpf(), dto.getEventoId());
        
        VendaSimulacaoResponseDto resposta = new VendaSimulacaoResponseDto();
        resposta.setCpfCliente(cliente.getCpf());
        resposta.setSaldoAtual(cliente.getSaldo());
        resposta.setAlertas(new ArrayList<>());
        
        BigDecimal totalCompra = BigDecimal.ZERO;

        for (ItemCarrinhoDto item : dto.getItens()) {
            Produto p = produtoRepository.findById(item.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto ID " + item.getProdutoId() + " não encontrado"));
            
            totalCompra = totalCompra.add(p.getValor().multiply(BigDecimal.valueOf(item.getQuantidade())));
            
            if (p.getQuantidadeEstoque() < item.getQuantidade()) {
                resposta.getAlertas().add("Estoque insuficiente para: " + p.getNome() + " (Restam: " + p.getQuantidadeEstoque() + ")");
            }
        }

        resposta.setTotalCompra(totalCompra);
        resposta.setSaldoFinalPrevisto(cliente.getSaldo().subtract(totalCompra));

        if (cliente.getSaldo().compareTo(totalCompra) < 0) {
            resposta.getAlertas().add("Saldo insuficiente. Faltam: R$ " + totalCompra.subtract(cliente.getSaldo()));
        }

        resposta.setVendaAprovada(resposta.getAlertas().isEmpty());

        return resposta;
    }

    @Transactional
    public Pedido processarVenda(CheckoutRequestDto checkoutDto, Usuario vendedorLogado) {
        Empresa empresa = empresaRepository.findById(checkoutDto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada."));

        boolean temPermissao = vendedorLogado.getEmpresas().stream()
                .anyMatch(emp -> emp.getId().equals(empresa.getId()));

        if (!temPermissao) {
            throw new IllegalStateException("Acesso negado. Você não tem permissão para realizar vendas nesta empresa.");
        }

        Cliente cliente = clienteRepository.findByCpf(checkoutDto.getCpf())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o CPF informado."));

        Pedido pedido = new Pedido();
        pedido.setEmpresa(empresa);
        pedido.setCliente(cliente);
        pedido.setItens(new ArrayList<>());
        
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        for (ItemCarrinhoDto itemDto : checkoutDto.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto ID " + itemDto.getProdutoId() + " não encontrado."));

            if (!produto.getEmpresa().getId().equals(empresa.getId())) {
                throw new IllegalStateException("O produto '" + produto.getNome() + "' não pertence a esta empresa.");
            }

            if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido); 
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDto.getQuantidade());
            itemPedido.setValorUnitario(produto.getValor());

            pedido.getItens().add(itemPedido);

            BigDecimal subTotal = produto.getValor().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            valorTotalPedido = valorTotalPedido.add(subTotal);
        }

        pedido.setValorTotal(valorTotalPedido);

        if (cliente.getSaldo().compareTo(valorTotalPedido) < 0) {
            throw new IllegalStateException("Saldo insuficiente! Saldo atual: " + cliente.getSaldo() + ", Total: " + valorTotalPedido);
        }

        cliente.setSaldo(cliente.getSaldo().subtract(valorTotalPedido));
        empresa.setCreditos(empresa.getCreditos().add(valorTotalPedido));

        clienteRepository.save(cliente);
        empresaRepository.save(empresa);
        
        return pedidoRepository.save(pedido);
    }
    
    @Transactional
    public void estornarVenda(Integer pedidoId, Usuario vendedorLogado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado."));
            
        
        Cliente cliente = pedido.getCliente();
        Empresa empresa = pedido.getEmpresa();
        
        cliente.setSaldo(cliente.getSaldo().add(pedido.getValorTotal()));
        empresa.setCreditos(empresa.getCreditos().subtract(pedido.getValorTotal()));
        
        
        clienteRepository.save(cliente);
        empresaRepository.save(empresa);
        pedidoRepository.delete(pedido); 
    }

    public List<Pedido> listarPedidosPorEmpresa(Integer empresaId, Usuario vendedorLogado) {
        return pedidoRepository.findByEmpresaIdOrderByDataPedidoDesc(empresaId);
    }
}