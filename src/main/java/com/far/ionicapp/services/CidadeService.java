package com.far.ionicapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.far.ionicapp.domain.Cidade;
import com.far.ionicapp.dto.CidadeDTO;
import com.far.ionicapp.repositories.CidadeRepository;
import com.far.ionicapp.services.exception.ObjectNotFoundException;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;

	public Cidade find(Integer id) {
		Optional<Cidade> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id : " + id + ", Tipo : " + Cidade.class.getName()));
	}

	public List<Cidade> findAll() {
		return repository.findAll();
	}

	public Cidade insert(Cidade obj) {
		obj.setId(null);
		return repository.save(obj);
	}
	
	public Cidade update(Cidade obj) {
		Cidade newObj = find(obj.getId());
		updateData(newObj,obj);
		return repository.save(newObj);
	}
	
	public void updateData(Cidade newObj, Cidade obj) {
		newObj.setNome(obj.getNome());
	}

	public void delete(Integer id) {
		find(id);
		repository.deleteById(id);
	}

	public Page<Cidade> findPage(Integer page,Integer linesPerPage,String orderBy,String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest) ;
	}
	
	public Cidade fromDTO(CidadeDTO objDTO) {
		throw new UnsupportedOperationException();
	}
	
	public List<Cidade> findByEstado(Integer estadoId) {
		return repository.findCidades(estadoId);
	}

}
