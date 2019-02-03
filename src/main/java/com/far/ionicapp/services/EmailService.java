 package com.far.ionicapp.services;

import org.springframework.mail.SimpleMailMessage;

import com.far.ionicapp.domain.Cliente;
import com.far.ionicapp.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);

}
