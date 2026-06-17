package com.ifbank.api_ifbank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "MOVIMENTACOES")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "DATA_MOVIMENTO", nullable = false)
    private LocalDate dataMovimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTA", nullable = false)
    private Conta conta;

    // Nullable: só preenchido em transferências
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTA_DESTINO")
    private Conta contaDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_MOVIMENTO", nullable = false)
    private TipoMovimento tipoMovimento;

    public Movimentacao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getDataMovimento() { return dataMovimento; }
    public void setDataMovimento(LocalDate dataMovimento) { this.dataMovimento = dataMovimento; }

    public Conta getConta() { return conta; }
    public void setConta(Conta conta) { this.conta = conta; }

    public Conta getContaDestino() { return contaDestino; }
    public void setContaDestino(Conta contaDestino) { this.contaDestino = contaDestino; }

    public TipoMovimento getTipoMovimento() { return tipoMovimento; }
    public void setTipoMovimento(TipoMovimento tipoMovimento) { this.tipoMovimento = tipoMovimento; }
}