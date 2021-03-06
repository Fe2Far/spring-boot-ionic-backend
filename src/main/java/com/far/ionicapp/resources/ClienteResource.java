package com.far.ionicapp.resources;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.dto.ClienteDTO;
import com.far.ionicapp.dto.ClienteNewDTO;
import com.far.ionicapp.services.ClienteService;
import com.far.ionicapp.services.exception.DataIntegrityException;

import javassist.tools.rmi.ObjectNotFoundException;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;

	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) throws ObjectNotFoundException {		
		Cliente obj = service.find(id); 
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll(){		
		List<Cliente> lista= service.findAll(); 
		List<ClienteDTO> listDTO = lista.stream()
				.map(obj -> new ClienteDTO(obj)).collect(Collectors.toList()); 
		return ResponseEntity.ok().body(listDTO);
	}

	/**
	 * 
	 * @param 
	 * 
	 * {
			"nome":"Felipe Almeida",
			"email":"felipe@gmail.com",
			"cpfOuCnpj":"00538696052",
			"tipoCliente": 1,
			"logradouro":"Rua Joao da Silva",
			"numero":"123",
			"complemento":"AP 111",
			"bairro":"CENTRO",
			"cep":"02040150",
			"cidade_id": 2,
		
			"telefone1":"11996089080",
			"telefone2":"1120503297"
		}
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO) {
		Cliente obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO,@PathVariable Integer id) {
		Cliente obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Integer id) throws ObjectNotFoundException {		
		
		try {
			service.delete(id);	
		}
		catch(DataIntegrityViolationException e) {
			 throw new DataIntegrityException("Não é possível excluir um cliente que possui relacionamentos.");
		}
		
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/page",method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page",defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage",defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBY",defaultValue="nome") String orderBy,
			@RequestParam(value="direction",defaultValue="ASC") String direction){		

		Page<Cliente> lista= service.findPage(page,linesPerPage,orderBy,direction); 
		Page<ClienteDTO> listDTO = lista.map(obj -> new ClienteDTO(obj)); 
		return ResponseEntity.ok().body(listDTO);
	}
	
	@RequestMapping(value="/email", method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@RequestParam(value="value") String email) {
		Cliente obj = service.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}
	
	

}
