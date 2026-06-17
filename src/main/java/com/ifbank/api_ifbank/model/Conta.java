package com.ifbank.api_ifbank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.ifbank.api_ifbank.model.enums.StatusConta;

@Entity
@Table(name = "CONTAS")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMERO_CONTA", nullable = false, length = 10)
    private String numeroConta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;

    @Column(name = "DATA_ABERTURA", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "ID_STATUS_CONTA", nullable = false)
    private Integer idStatusConta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", nullable = false, unique = true)
    private Cliente cliente;

    public Conta() {}

    public Conta(Long id, String numeroConta, BigDecimal saldo, LocalDate dataAbertura,
                 Integer idStatusConta, Cliente cliente) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.saldo = saldo;
        this.dataAbertura = dataAbertura;
        this.idStatusConta = idStatusConta;
        this.cliente = cliente;
    }

    // Getter/Setter do campo real persistido
    public Integer getIdStatusConta() {
        return idStatusConta;
    }

    public void setIdStatusConta(Integer idStatusConta) {
        this.idStatusConta = idStatusConta;
    }

    // Conveniência: lê o enum a partir do id salvo
    @Transient
    public StatusConta getStatusConta() {
        return StatusConta.fromId(this.idStatusConta);
    }

    // Conveniência: salva o id do enum no campo persistido
    public void setStatusConta(StatusConta status) {
        this.idStatusConta = status.getId();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}