package com.far.ionicapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.dto.ClienteDTO;
import com.far.ionicapp.repositories.ClienteRepository;
import com.far.ionicapp.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		//return obj.orElse(null);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id : " + id + ", Tipo : " + Cliente.class.getName()));
	}
	
	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repository.save(obj);
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateDate(newObj,obj);
		return repository.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		repository.deleteById(id);
	}

	public Page<Cliente> findPage(Integer page,Integer linesPerPage,String orderBy,String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest) ;
	}

	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null); 
	}
	
	public void updateDate(Cliente newObj,Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
