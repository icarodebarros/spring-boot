package com.icarodebarros.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.icarodebarros.cursomc.services.DBService;
import com.icarodebarros.cursomc.services.EmailService;
import com.icarodebarros.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;

	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		
		return true;
	}
	
	/*
	    A anotação @Bean torma esse método disponível como componente no sistema, ou seja, quando existirem injeções de dependências (@Autowired)
	    do tipo EmailService o sistema vai procurar por um componente que devolva uma instância desse tipo.
	    No caso o Spring vai encontrar a anotação abaixo e devolver uma instância de MockEmailService, tudo de forma automática.	    
	 */
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
