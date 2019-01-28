package com.far.ionicapp.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import com.far.ionicapp.components.Messages;
import com.far.ionicapp.domain.Categoria;

public class CategoriaDTO implements Serializable {
	
	@Autowired
    Messages messages;

	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotEmpty(message="Preenchimento Obrigatório")
	@Length(min = 5, max = 12, message = "Tamanho minimo entre 5 e maximo de 12 caracteres")
	private String nome;
	
	
	public CategoriaDTO() {
		super();
	}
	
	public CategoriaDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.nome = categoria.getNome();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	
	
}
