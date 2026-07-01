package com.ifbank.api_ifbank.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Gerentes")
public class Gerente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@NotNull(message = "O nome é obrigatório")
	@Column(name = "nome")
    private String nome;
	
	@NotNull(message = "A data de nascimento é obrigatório")
	@Column(name= "data_nascimento")
    private LocalDate dataNascimento;

	@OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
	
	public Gerente() {}
	public Gerente(Long id, @NotNull(message = "O nome é obrigatório") String nome,
			@NotNull(message = "A data de nascimento é obrigatório") LocalDate dataNascimento, Usuario usuario) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.usuario = usuario;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
