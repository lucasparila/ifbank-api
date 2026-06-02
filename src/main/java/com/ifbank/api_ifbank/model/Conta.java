package com.ifbank.api_ifbank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "Contas")
public class Conta {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numeroConta")
    private String numeroConta;
    
    private BigDecimal saldo;
    
    @Column(name = "dataAbertura")
    private LocalDate dataAbertura;
    
    @Column(name = "idStatusConta")
    private Integer idStatusConta; 

    @OneToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false, unique = true)
    private Cliente cliente; 
    
    public Conta() {}

	public Conta(Long id, String numeroConta, BigDecimal saldo, LocalDate dataAbertura, Integer idStatusConta,
			Cliente cliente) {
		super();
		this.id = id;
		this.numeroConta = numeroConta;
		this.saldo = saldo;
		this.dataAbertura = dataAbertura;
		this.idStatusConta = idStatusConta;
		this.cliente = cliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getIdStatusConta() {
		return idStatusConta;
	}

	public void setIdStatusConta(Integer idStatusConta) {
		this.idStatusConta = idStatusConta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
    
    
}
