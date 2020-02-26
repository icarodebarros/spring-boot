package com.icarodebarros.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.icarodebarros.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
