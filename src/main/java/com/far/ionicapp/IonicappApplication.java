package com.far.ionicapp;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.far.ionicapp.domain.Categoria;
import com.far.ionicapp.domain.Cidade;
import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.domain.Endereco;
import com.far.ionicapp.domain.Estado;
import com.far.ionicapp.domain.Pagamento;
import com.far.ionicapp.domain.PagamentoComBoleto;
import com.far.ionicapp.domain.PagamentoComCartao;
import com.far.ionicapp.domain.Pedido;
import com.far.ionicapp.domain.Produto;
import com.far.ionicapp.domain.enums.EstadoPagamento;
import com.far.ionicapp.domain.enums.TipoCliente;
import com.far.ionicapp.repositories.CategoriaRepository;
import com.far.ionicapp.repositories.CidadeRepository;
import com.far.ionicapp.repositories.ClienteRepository;
import com.far.ionicapp.repositories.EnderecoRepository;
import com.far.ionicapp.repositories.EstadoRepository;
import com.far.ionicapp.repositories.PagamentoRepository;
import com.far.ionicapp.repositories.PedidoRepository;
import com.far.ionicapp.repositories.ProdutoRepository;

@SpringBootApplication
public class IonicappApplication  implements CommandLineRunner {
	
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

	public static void main(String[] args) {
		SpringApplication.run(IonicappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null,"Informática");
		Categoria cat2 = new Categoria(null,"Escritório");
		
		Produto p1 = new Produto(null,"Computador",2000.00);
		Produto p2 = new Produto(null,"Impressora",1100.00);
		Produto p3 = new Produto(null,"Computador",80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"São Paulo");

		Cidade c1 = new Cidade(null,"Uberlandia",est1);
		Cidade c2 = new Cidade(null,"Campinas",est2);
		Cidade c3 = new Cidade(null,"São Paulo",est2);

		est1.getCidades().addAll(Arrays.asList(c1));		
		est2.getCidades().addAll(Arrays.asList(c2,c3));

		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));

		Cliente cli1 = new Cliente(null,"Maria","maria@gmail.com","123456789",TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("23456789","9654321"));

		Endereco e1 = new Endereco(null,"Rua Flores","300","Apt 303","Jardim","38242254",cli1,c1);
		Endereco e2 = new Endereco(null,"Av Matos","105","Sl 800","Centro","3877012",cli1,c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));

		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		
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

	}

}

