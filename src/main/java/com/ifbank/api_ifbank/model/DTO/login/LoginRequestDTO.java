package com.ifbank.api_ifbank.model.DTO.login;

public class LoginRequestDTO {
    private String email;
    private String senha;
    
    public LoginRequestDTO() {
    	
    }
	public LoginRequestDTO(String email, String senha) {
		super();
		this.email = email;
		this.senha = senha;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
    
    
}

