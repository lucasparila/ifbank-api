package com.ifbank.api_ifbank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Telefones")
public class Telefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@NotNull(message = "o código do país é obrigatório")
	@Column(name = "cod_pais")
	private Integer codPais;
	
	@NotNull(message = "o código da área é obrigatório")
	@Column(name = "cod_area")
	private Integer codArea;
	
	@NotNull(message = "o  número é obrigatório")
	@Column(name = "numero")
	private Integer numero;
	
	public Telefone() {}

	public Telefone(Long id, @NotNull(message = "o código do país é obrigatório") Integer codPais,
			@NotNull(message = "o código da área é obrigatório") Integer codArea,
			@NotNull(message = "o  número é obrigatório") Integer numero) {
		super();
		this.id = id;
		this.codPais = codPais;
		this.codArea = codArea;
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCodPais() {
		return codPais;
	}

	public void setCodPais(Integer codPais) {
		this.codPais = codPais;
	}

	public Integer getCodArea() {
		return codArea;
	}

	public void setCodArea(Integer codArea) {
		this.codArea = codArea;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	

}
