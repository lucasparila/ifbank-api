package com.ifbank.api_ifbank.model.DTO.movimentacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoResponseDTO {

    private Long id;
    private String tipoMovimento;
    private BigDecimal valor;
    private BigDecimal saldoAtualizado;
    private LocalDate dataMovimento;
    private String nomeCliente;
    private String emailCliente;
    private String contaDestino;

    public MovimentacaoResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoMovimento() { return tipoMovimento; }
    public void setTipoMovimento(String tipoMovimento) { this.tipoMovimento = tipoMovimento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getSaldoAtualizado() { return saldoAtualizado; }
    public void setSaldoAtualizado(BigDecimal saldoAtualizado) { this.saldoAtualizado = saldoAtualizado; }

    public LocalDate getDataMovimento() { return dataMovimento; }
    public void setDataMovimento(LocalDate dataMovimento) { this.dataMovimento = dataMovimento; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public String getContaDestino() { return contaDestino; }
    public void setContaDestino(String contaDestino) { this.contaDestino = contaDestino; }
}