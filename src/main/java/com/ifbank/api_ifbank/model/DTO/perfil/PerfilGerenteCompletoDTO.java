package com.ifbank.api_ifbank.model.DTO.perfil;

import com.ifbank.api_ifbank.model.DTO.gerente.GerenteDTO;

public class PerfilGerenteCompletoDTO {
	private Long idUsuario;
    private String email;
    private String cpf;
    private String perfil;
    private GerenteDTO gerente;
    
    public PerfilGerenteCompletoDTO() {}

	public PerfilGerenteCompletoDTO(Long idUsuario, String email, String cpf, String perfil, GerenteDTO geretenDTO) {
		super();
		this.idUsuario = idUsuario;
		this.email = email;
		this.cpf = cpf;
		this.perfil = perfil;
		this.gerente = geretenDTO;
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

	public GerenteDTO getGerente() {
		return this.gerente;
	}

	public void setGerente(GerenteDTO geretenDTO) {
		this.gerente = geretenDTO;
	}
    
	
}
