package com.ifbank.api_ifbank.model.DTO.cliente;

import java.time.LocalDate;

public class ClienteDTO {

	private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataCadastro;
    private String fotoUrl;
    private EnderecoDTO endereco;
    private TelefoneDTO telefone;
    
	public ClienteDTO(Long id, String nome, LocalDate dataNascimento, LocalDate dataCadastro,String fotoUrl, EnderecoDTO endereco,TelefoneDTO telefone) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.dataCadastro = dataCadastro;
		this.fotoUrl = fotoUrl;
		this.endereco = endereco;
		this.telefone = telefone;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFotoUrl() {
		return fotoUrl;
	}

	public void setFotoUrl(String fotoUrl) {
		this.fotoUrl = fotoUrl;
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

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public EnderecoDTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoDTO endereco) {
		this.endereco = endereco;
	}

	public TelefoneDTO getTelefone() {
		return telefone;
	}

	public void setTelefone(TelefoneDTO telefone) {
		this.telefone = telefone;
	}
	
	
    
    
}
