package com.ifbank.api_ifbank.service.interfaces;

import org.springframework.scheduling.annotation.Async;

import com.ifbank.api_ifbank.model.Cliente;
import com.ifbank.api_ifbank.model.Conta;

public interface IEmailService {
	
	public void enviarEmailResetSenha(String destinatario, String token);
	
	@Async
	public void enviarEmailCadastro(Cliente clienteCadastrado, String emailDestinatario);
	
	@Async
	public void enviarEmailContaAprovada(Cliente cliente, Conta conta, String emailDestinatario);
	
	@Async
	public void enviarEmailContaReprovada(Cliente cliente,String emailDestinatario);

}
