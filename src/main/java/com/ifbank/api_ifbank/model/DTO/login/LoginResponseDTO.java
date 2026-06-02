package com.ifbank.api_ifbank.model.DTO.login;

public class LoginResponseDTO {
	
	 private Long idUsuario;
	 private String cpf;
	 private String email;
	 private String perfil;
	 
	 public LoginResponseDTO() {}
	 public LoginResponseDTO(Long idUsuario, String cpf, String email, String perfil) {
		this.idUsuario = idUsuario;
		this.cpf = cpf;
		this.email = email;
		this.perfil = perfil;
	}
	 public Long getIdUsuario() {
		 return idUsuario;
	 }
	 public void setIdUsuario(Long idUsuario) {
		 this.idUsuario = idUsuario;
	 }
	 
	 public String getCpf() {
		return cpf;
	}
	 public void setCpf(String cpf) {
		 this.cpf = cpf;
	 }
	 public String getEmail() {
		 return email;
	 }
	 public void setEmail(String email) {
		 this.email = email;
	 }
	 public String getPerfil() {
		 return perfil;
	 }
	 public void setPerfil(String perfil) {
		 this.perfil = perfil;
	 } 
}
