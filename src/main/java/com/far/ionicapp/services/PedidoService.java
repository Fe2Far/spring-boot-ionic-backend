package com.far.ionicapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.far.ionicapp.domain.Pedido;
import com.far.ionicapp.repositories.PedidoRepository;
import com.far.ionicapp.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repository.findById(id);
		//return obj.orElse(null);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id : " + id + ", Tipo : " + Pedido.class.getName()));
	}

}
