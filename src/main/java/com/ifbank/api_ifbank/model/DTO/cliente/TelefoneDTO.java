package com.ifbank.api_ifbank.model.DTO.cliente;

public class TelefoneDTO {
	
	private Integer codPais;
    private Integer codArea;
    private Integer numero;
    
    public TelefoneDTO() {}
	public TelefoneDTO(Integer codPais, Integer codArea, Integer numero) {
		super();
		this.codPais = codPais;
		this.codArea = codArea;
		this.numero = numero;
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
