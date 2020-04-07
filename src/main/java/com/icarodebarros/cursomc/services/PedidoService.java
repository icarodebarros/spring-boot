package com.icarodebarros.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.Cliente;
import com.icarodebarros.cursomc.domain.ItemPedido;
import com.icarodebarros.cursomc.domain.PagamentoComBoleto;
import com.icarodebarros.cursomc.domain.Pedido;
import com.icarodebarros.cursomc.domain.enums.EstadoPagamento;
import com.icarodebarros.cursomc.repositories.ItemPedidoRepository;
import com.icarodebarros.cursomc.repositories.PagamentoRepository;
import com.icarodebarros.cursomc.repositories.PedidoRepository;
import com.icarodebarros.cursomc.security.UserSS;
import com.icarodebarros.cursomc.services.exceptions.AuthorizationException;

@Service
public class PedidoService extends GenericService<Pedido, Integer> {
		
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public PedidoRepository getRepository() {
		return (PedidoRepository) super.getRepository();
	}
	
	@Override
	protected void preDependencies(Pedido obj) {
		if (obj.getId() == null) {
			obj.setInstante(new Date());
			obj.setCliente(clienteService.find(obj.getCliente().getId()));
			obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
			obj.getPagamento().setPedido(obj);
			if (obj.getPagamento() instanceof PagamentoComBoleto) {
				PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
				boletoService.peencherPagamentoComBoleto(pagto, obj.getInstante());
			}			
		}
	}
	
	@Override
	protected void postDependencies(Pedido obj) {
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		//emailService.sendOderConfirmationEmail(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);
	}
	
	@Override
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		
		return getRepository().findByCliente(cliente, pageRequest);
	}

}
