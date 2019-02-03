package com.far.ionicapp.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.domain.ItemPedido;
import com.far.ionicapp.domain.PagamentoComBoleto;
import com.far.ionicapp.domain.Pedido;
import com.far.ionicapp.domain.enums.EstadoPagamento;
import com.far.ionicapp.repositories.ItemPedidoRepository;
import com.far.ionicapp.repositories.PagamentoRepository;
import com.far.ionicapp.repositories.PedidoRepository;
import com.far.ionicapp.security.UserSS;
import com.far.ionicapp.security.UserService;
import com.far.ionicapp.services.exception.AuthorizationException;
import com.far.ionicapp.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
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

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		//return obj.orElse(null);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id : " + id + ", Tipo : " + Pedido.class.getName()));
	}
	

	
	/**
	 * 
	 * @param obj
	 * {
			"cliente" :{ "id":1},"enderecoDeEntrega":{"id":1},
			"pagamento" : {
				"numeroDeParcelas" : 10,
				"@type":"pagamentoComCartao"
			},
			"itens" : [
			{	
				"quantidade" : 2,
				"produto" : {"id":3}
			},	
			{	
				"quantidade" : 2,
				"produto" : {"id":3}
			}	
			]
		}
	 * @return
	 */
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}

		itemPedidoRepository.saveAll(obj.getItens());

		emailService.sendOrderConfirmationEmail(obj);

		return obj;
	}
	
	public Page<Pedido> findPage(Integer page,Integer linesPerPage,String orderBy,String direction) {
		
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		Cliente cliente =  clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
		
	}

}
