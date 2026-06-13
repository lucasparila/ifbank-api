package com.ifbank.api_ifbank.model.DTO.gerente;

import java.time.LocalDate;

public class GerenteDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    
    public GerenteDTO() {}

	public GerenteDTO(Long id, String nome, LocalDate dataNascimento) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
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
	
	
    
}
