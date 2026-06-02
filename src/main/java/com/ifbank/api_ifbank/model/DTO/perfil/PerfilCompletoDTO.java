package com.ifbank.api_ifbank.model.DTO.perfil;

import com.ifbank.api_ifbank.model.DTO.cliente.ClienteDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.ContaDTO;

public class PerfilCompletoDTO {

	private Long idUsuario;
    private String email;
    private String cpf;
    private String perfil;
    private ClienteDTO cliente;
    private ContaDTO conta;
    
	public PerfilCompletoDTO(Long idUsuario, String email, String cpf, String perfil,ClienteDTO cliente, ContaDTO conta) {
		super();
		this.idUsuario = idUsuario;
		this.email = email;
		this.cpf = cpf;
		this.perfil = perfil;
		this.cliente = cliente;
		this.conta = conta;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public ClienteDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}

	public ContaDTO getConta() {
		return conta;
	}

	public void setConta(ContaDTO conta) {
		this.conta = conta;
	}
    
	
    
}
