package com.far.ionicapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.far.ionicapp.domain.Cidade;
import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.domain.Endereco;
import com.far.ionicapp.domain.enums.TipoCliente;
import com.far.ionicapp.dto.ClienteDTO;
import com.far.ionicapp.dto.ClienteNewDTO;
import com.far.ionicapp.repositories.ClienteRepository;
import com.far.ionicapp.repositories.EnderecoRepository;
import com.far.ionicapp.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id : " + id + ", Tipo : " + Cliente.class.getName()));
	}
	
	public List<Cliente> findAll() {
		return repository.findAll();
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
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
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null,null); 
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		
		Cliente cli = new Cliente(
				null, 
				objDTO.getNome(), 
				objDTO.getEmail(), 
				objDTO.getCpfOuCnpj(), 
				TipoCliente.toEnum(objDTO.getTipoCliente()),
				pe.encode(objDTO.getSenha())
				);
 
		Endereco end = new Endereco(
				null, 
				objDTO.getLogradouro(), 
				objDTO.getNumero(), 
				objDTO.getComplemento(), 
				objDTO.getBairro(), 
				objDTO.getCep(), 
				cli, 
				new Cidade(objDTO.getCidade_id(),null,null)
				);

		//Adiciona endereços ao objeto cliente
		cli.getEnderecos().add(end);

		cli.getTelefones().add(objDTO.getTelefone1());

		if(objDTO.getTelefone2() != null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		
		if(objDTO.getTelefone3() != null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		
		return cli;
	}
	
	public void updateDate(Cliente newObj,Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
