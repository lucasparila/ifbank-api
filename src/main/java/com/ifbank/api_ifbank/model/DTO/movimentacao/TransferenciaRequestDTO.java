package com.ifbank.api_ifbank.model.DTO.movimentacao;

import java.math.BigDecimal;

public class TransferenciaRequestDTO {

    private Long idConta;
    private String numeroContaDestino;
    private BigDecimal valor;

    public TransferenciaRequestDTO() {}

    public Long getIdConta() { return idConta; }
    public void setIdConta(Long idConta) { this.idConta = idConta; }

    public String getNumeroContaDestino() { return numeroContaDestino; }
    public void setNumeroContaDestino(String numeroContaDestino) { this.numeroContaDestino = numeroContaDestino; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}