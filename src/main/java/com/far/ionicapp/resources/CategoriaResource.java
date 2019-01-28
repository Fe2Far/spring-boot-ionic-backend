package com.far.ionicapp.resources;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.far.ionicapp.domain.Categoria;
import com.far.ionicapp.dto.CategoriaDTO;
import com.far.ionicapp.services.CategoriaService;
import com.far.ionicapp.services.exception.DataIntegrityException;

import javassist.tools.rmi.ObjectNotFoundException;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Categoria > find(@PathVariable Integer id) throws ObjectNotFoundException {		
		Categoria obj = service.find(id); 
		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll(){		
		List<Categoria> lista= service.findAll(); 
		List<CategoriaDTO> listDTO = lista.stream()
				.map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList()); 
		return ResponseEntity.ok().body(listDTO);
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO) {

		Categoria obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDTO,@PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Integer id) throws ObjectNotFoundException {		
		
		try {
			service.delete(id);	
		}
		catch(DataIntegrityViolationException e) {
			 throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos associados");
		}
		
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value="/page",method=RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page",defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage",defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBY",defaultValue="nome") String orderBy,
			@RequestParam(value="direction",defaultValue="ASC") String direction){		

		Page<Categoria> lista= service.findPage(page,linesPerPage,orderBy,direction); 
		Page<CategoriaDTO> listDTO = lista.map(obj -> new CategoriaDTO(obj)); 
		return ResponseEntity.ok().body(listDTO);
	}

}
