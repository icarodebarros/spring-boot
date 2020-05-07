package com.icarodebarros.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.icarodebarros.cursomc.services.EmailService;
import com.icarodebarros.cursomc.services.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
		
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instantiateDatabase() throws ParseException {			
		
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
