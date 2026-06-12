package com.ifbank.api_ifbank.model.DTO.cliente;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaDTO {
	private String numeroConta;
    private BigDecimal saldo;
    private LocalDate dataAbertura;
    private String statusConta;
    
	public ContaDTO(String numeroConta, BigDecimal saldo, LocalDate dataAbertura, String statusConta) {
		this.numeroConta = numeroConta;
		this.saldo = saldo;
		this.dataAbertura = dataAbertura;
		this.statusConta = statusConta;
	}

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public LocalDate getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDate dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public String getIdStatusConta() {
		return this.statusConta;
	}

	public void setIdStatusConta(String statusConta) {
		this.statusConta = statusConta;
	}
}
