package com.ifbank.api_ifbank.model.DTO.movimentacao;

import java.math.BigDecimal;

public class MovimentacaoRequestDTO {

    private Long idConta;
    private BigDecimal valor;

    public MovimentacaoRequestDTO() {}

    public Long getIdConta() { return idConta; }
    public void setIdConta(Long idConta) { this.idConta = idConta; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}