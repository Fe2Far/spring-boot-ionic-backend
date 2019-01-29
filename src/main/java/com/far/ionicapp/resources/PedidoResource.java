package com.far.ionicapp.resources;


import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.far.ionicapp.domain.Pedido;
import com.far.ionicapp.services.PedidoService;

import javassist.tools.rmi.ObjectNotFoundException;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;

	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) throws ObjectNotFoundException {		
		Pedido obj = service.find(id); 
		return ResponseEntity.ok().body(obj);
	}
	
	/**
	 * 
	 * @param 
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
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

}
