package com.ifbank.api_ifbank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Enderecos")
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;
	
	@NotNull(message="O nome do logradouro é obrigatório")
	@Column(name = "logradouro")
	private String logradouro;
	
	@Column(name = "numero")
    private Integer numero;
	
	@Column(name = "complemento")
    private String complemento;
	
	@NotNull(message = "O nome do bairro é obrigatório")
	@Column(name = "bairro")
    private String bairro;
	
	@NotNull(message = "O nome da cidade é obrigatório")
	@Column(name = "cidade")
    private String cidade;
	
	@NotNull(message = "O estado é obrigatório")
	@Column(name = "estado")
    private String estado;
	
	@NotNull(message = "cep é obrigatório")
	@Column(name = "cep")
    private String cep;
	
	public Endereco() {}

	public Endereco(Long id, @NotNull(message = "O nome do logradouro é obrigatório") String logradouro, Integer numero,
			String complemento, @NotNull(message = "O nome do bairro é obrigatório") String bairro,
			@NotNull(message = "O nome da cidade é obrigatório") String cidade,
			@NotNull(message = "O estado é obrigatório") String estado,
			@NotNull(message = "cep é obrigatório") String cep) {
		super();
		this.id = id;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	
}
