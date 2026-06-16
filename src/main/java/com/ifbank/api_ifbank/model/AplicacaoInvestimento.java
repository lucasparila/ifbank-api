package com.ifbank.api_ifbank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "AplicacoesInvestimentos")
public class AplicacaoInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_aplicado")
    private BigDecimal valorAplicado;

    @Column(name = "data_aplicacao")
    private LocalDate dataAplicacao;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_conta", referencedColumnName = "id", nullable = false)
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "id_tipo_investimento", referencedColumnName = "id", nullable = false)
    private TipoInvestimento tipoInvestimento;

    public AplicacaoInvestimento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValorAplicado() { return valorAplicado; }
    public void setValorAplicado(BigDecimal valorAplicado) { this.valorAplicado = valorAplicado; }

    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Conta getConta() { return conta; }
    public void setConta(Conta conta) { this.conta = conta; }

    public TipoInvestimento getTipoInvestimento() { return tipoInvestimento; }
    public void setTipoInvestimento(TipoInvestimento tipoInvestimento) { this.tipoInvestimento = tipoInvestimento; }
}