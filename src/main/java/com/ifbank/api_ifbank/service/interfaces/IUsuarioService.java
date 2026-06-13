package com.ifbank.api_ifbank.service.interfaces;

import com.ifbank.api_ifbank.model.DTO.cadastro.CadastroClienteRequestDTO;
import com.ifbank.api_ifbank.model.DTO.login.LoginRequestDTO;
import com.ifbank.api_ifbank.model.DTO.login.LoginResponseDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilClienteCompletoDTO;

public interface IUsuarioService {
	LoginResponseDTO autenticar(LoginRequestDTO dadosLogin);
	String cadastrarCliente(CadastroClienteRequestDTO dto);
	PerfilClienteCompletoDTO obterPerfilCompleto(Long idUsuario);
}
