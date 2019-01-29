package com.far.ionicapp.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.domain.enums.TipoCliente;
import com.far.ionicapp.dto.ClienteNewDTO;
import com.far.ionicapp.repositories.ClienteRepository;
import com.far.ionicapp.resources.exception.FieldMessage;
import com.far.ionicapp.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
		
	}

	@Override
	public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		if(objDTO.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDTO.getCpfOuCnpj())){
			list.add(new FieldMessage("cpfOuCnpj","CPF Inválido.")); 
		}

		if(objDTO.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())){
			list.add(new FieldMessage("cpfOuCnpj","CNPJ Inválido.")); 
		}

		Cliente aux  = repo.findByEmail(objDTO.getEmail());
		if(aux != null) {
			list.add(new FieldMessage("email", "Email já existente."));
		}

		for(FieldMessage  e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
			.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
