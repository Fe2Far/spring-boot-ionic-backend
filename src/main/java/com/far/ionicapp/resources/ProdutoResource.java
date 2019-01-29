package com.far.ionicapp.resources;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.far.ionicapp.domain.Produto;
import com.far.ionicapp.dto.ProdutoDTO;
import com.far.ionicapp.resources.utils.URL;
import com.far.ionicapp.services.ProdutoService;

import javassist.tools.rmi.ObjectNotFoundException;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;

	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) throws ObjectNotFoundException {		
		Produto obj = service.find(id); 
		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value="nome",defaultValue="") String nome,
			@RequestParam(value="categorias",defaultValue="") String categorias,
			@RequestParam(value="page",defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage",defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBY",defaultValue="nome") String orderBy,
			@RequestParam(value="direction",defaultValue="ASC") String direction){		

		List<Integer> ids = URL.decodeIntList(categorias);
		String nomeDecoded  = URL.decodeParam(nome);

		Page<Produto> lista= service.search(nomeDecoded,ids,page,linesPerPage,orderBy,direction); 
		Page<ProdutoDTO> listDTO = lista.map(obj -> new ProdutoDTO(obj)); 
		return ResponseEntity.ok().body(listDTO);
	}

}
