package com.far.ionicapp.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.far.ionicapp.domain.Categoria;
import com.far.ionicapp.domain.Cidade;
import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.domain.Endereco;
import com.far.ionicapp.domain.Estado;
import com.far.ionicapp.domain.ItemPedido;
import com.far.ionicapp.domain.Pagamento;
import com.far.ionicapp.domain.PagamentoComBoleto;
import com.far.ionicapp.domain.PagamentoComCartao;
import com.far.ionicapp.domain.Pedido;
import com.far.ionicapp.domain.Produto;
import com.far.ionicapp.domain.enums.EstadoPagamento;
import com.far.ionicapp.domain.enums.Perfil;
import com.far.ionicapp.domain.enums.TipoCliente;
import com.far.ionicapp.repositories.CategoriaRepository;
import com.far.ionicapp.repositories.CidadeRepository;
import com.far.ionicapp.repositories.ClienteRepository;
import com.far.ionicapp.repositories.EnderecoRepository;
import com.far.ionicapp.repositories.EstadoRepository;
import com.far.ionicapp.repositories.ItemPedidoRepository;
import com.far.ionicapp.repositories.PagamentoRepository;
import com.far.ionicapp.repositories.PedidoRepository;
import com.far.ionicapp.repositories.ProdutoRepository;


@Service
public class DBService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public void instatiateTestDatabase() {
		
		try {
		
		Categoria cat1 = new Categoria(null,"Informática");
		Categoria cat2 = new Categoria(null,"Escritório");
		Categoria cat3 = new Categoria(null,"Cama Mesa e Banho");
		Categoria cat4 = new Categoria(null,"Jardinagem");
		Categoria cat5 = new Categoria(null,"Eletronicos");
		Categoria cat6 = new Categoria(null,"Games");
		Categoria cat7 = new Categoria(null,"Livros");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		Produto p4 = new Produto(null, "Mesa de escritório", 300.00);
		Produto p5 = new Produto(null, "Toalha", 50.00);
		Produto p6 = new Produto(null, "Colcha", 200.00);
		Produto p7 = new Produto(null, "TV true color", 1200.00);
		Produto p8 = new Produto(null, "Roçadeira", 800.00);
		Produto p9 = new Produto(null, "Abajour", 100.00);
		Produto p10 = new Produto(null, "Pendente", 180.00);
		Produto p11 = new Produto(null, "Shampoo", 90.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		cat2.getProdutos().addAll(Arrays.asList(p2, p4));
		cat3.getProdutos().addAll(Arrays.asList(p5, p6));
		cat4.getProdutos().addAll(Arrays.asList(p1, p2, p3, p7));
		cat5.getProdutos().addAll(Arrays.asList(p8));
		cat6.getProdutos().addAll(Arrays.asList(p9, p10));
		cat7.getProdutos().addAll(Arrays.asList(p11));

		p1.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2, cat4));
		p3.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p4.getCategorias().addAll(Arrays.asList(cat2));
		p5.getCategorias().addAll(Arrays.asList(cat3));
		p6.getCategorias().addAll(Arrays.asList(cat3));
		p7.getCategorias().addAll(Arrays.asList(cat4));
		p8.getCategorias().addAll(Arrays.asList(cat5));
		p9.getCategorias().addAll(Arrays.asList(cat6));
		p10.getCategorias().addAll(Arrays.asList(cat6));
		p11.getCategorias().addAll(Arrays.asList(cat7));	
		
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"São Paulo");

		Cidade c1 = new Cidade(null,"Uberlandia",est1);
		Cidade c2 = new Cidade(null,"Campinas",est2);
		Cidade c3 = new Cidade(null,"São Paulo",est2);
		Cidade c4 = new Cidade(null,"Guarulhos",est2);

		est1.getCidades().addAll(Arrays.asList(c1));		
		est2.getCidades().addAll(Arrays.asList(c2,c3));

		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));
		
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3,c4));

		Cliente cli1 = new Cliente(null,"Maria","maria@gmail.com","123456789",TipoCliente.PESSOAFISICA,pe.encode("!@#@!#!@DSA"));
		cli1.getTelefones().addAll(Arrays.asList("23456789","9654321"));

		
		Cliente cli2 = new Cliente(null,"Ana","ana@gmail.com","31628382740",TipoCliente.PESSOAFISICA,pe.encode("!@#@!#!@DSA"));
		cli2.getTelefones().addAll(Arrays.asList("99960870","34525252"));
		cli2.addPerfil(Perfil.ADMIN);
		
		
		Endereco e1 = new Endereco(null,"Rua Flores","300","Apt 303","Jardim","38242254",cli1,c1);
		Endereco e2 = new Endereco(null,"Av Matos","105","Sl 800","Centro","3877012",cli1,c2);
		
		Endereco e3 = new Endereco(null,"Av Brasil","2106",null,"Centro","0704040",cli1,c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		cli2.getEnderecos().addAll(Arrays.asList(e3));

		clienteRepository.saveAll(Arrays.asList(cli1,cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2,e3));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Pedido ped1 = new Pedido(null, sdf.parse("27/01/2019 15:31"),cli1,e1);
		
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2019 15:31"),cli1,e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1,6);
		ped1.setPagamento(pagto1);
		
		
		Pagamento pagto2 = new PagamentoComBoleto(null,EstadoPagamento.PENDENTE, ped2,sdf.parse("10/12/2019 15:31"),null);
		ped2.setPagamento(pagto2);

		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1,ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1,pagto2));

		ItemPedido ip1  = new ItemPedido(ped1, p1, 0.00, 1, 800.00);
		ItemPedido ip2 = new ItemPedido(ped1,p3,0.00,2,90.00);

		ItemPedido ip3 = new ItemPedido(ped2,p2,100.00,1,8000.00);

		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));

		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));

		itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));
		
		}  catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
