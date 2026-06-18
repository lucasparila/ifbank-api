package com.ifbank.api_ifbank.model;

import org.hibernate.validator.constraints.br.CPF;

import com.ifbank.api_ifbank.model.enums.TipoUsuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@NotNull(message = "O CPF é obrigatório")
	@CPF(message = "CPF inválido")
	@Column(name = "CPF",nullable = false, unique = true, length = 11)
    private String cpf;

	@NotNull(message = "A senha é obrigatório")
    @Column(name="senha",nullable = false, length = 100)
    private String senha; 

	@NotNull(message = "O email é obrigatório")
	@Email(message = "Email inválido")
    @Column(name = "email",nullable = false, unique = true, length = 150)
    private String email;

	@NotNull(message = "O tipo de usuário é obrigatório")
    @Column(name = "id_tipo_usuario",nullable = false)
    private Long idTipoUsuario;
	
	public Usuario() {}
	
	public Usuario(Long id, @NotNull(message = "O CPF é obrigatório") @CPF(message = "CPF inválido") String cpf,
			@NotNull(message = "A senha é obrigatório") String senha,
			@NotNull(message = "O email é obrigatório") @Email(message = "Email inválido") String email,
			@NotNull(message = "O tipo de usuário é obrigatório") Long idTipoUsuario) {
		this.id = id;
		this.cpf = cpf;
		this.senha = senha;
		this.email = email;
		this.idTipoUsuario = idTipoUsuario;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getIdTipoUsuario() {
		return idTipoUsuario;
	}

	public void setIdTipoUsuario(Long idTipoUsuario) {
		this.idTipoUsuario = idTipoUsuario;
	}

	public TipoUsuario getTipoUsuario() {
        return TipoUsuario.fromId(this.idTipoUsuario);
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.idTipoUsuario = tipoUsuario.getId();
    }
    
    
}
