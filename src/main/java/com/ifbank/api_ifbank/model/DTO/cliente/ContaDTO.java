package com.ifbank.api_ifbank.model.DTO.cliente;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaDTO {

    private Long id;
    private String numeroConta;
    private BigDecimal saldo;
    private LocalDate dataAbertura;
    private String statusConta;

    public ContaDTO() {}

    public ContaDTO(Long id, String numeroConta, BigDecimal saldo, LocalDate dataAbertura, String statusConta) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.saldo = saldo;
        this.dataAbertura = dataAbertura;
        this.statusConta = statusConta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public String getStatusConta() { return statusConta; }
    public void setStatusConta(String statusConta) { this.statusConta = statusConta; }
}

