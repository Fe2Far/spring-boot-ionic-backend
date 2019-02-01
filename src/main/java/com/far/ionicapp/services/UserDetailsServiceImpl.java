package com.far.ionicapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.repositories.ClienteRepository;
import com.far.ionicapp.security.UserSS;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cli = clienteRepository.findByEmail(email);
		if(cli == null) {
			throw new UsernameNotFoundException("Usuário não encontrado : " + email);
		}
		return new UserSS(cli.getId(),cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}

}
