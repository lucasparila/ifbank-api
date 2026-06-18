package com.ifbank.api_ifbank.model.DTO.investimento;

import java.math.BigDecimal;

public class AplicarInvestimentoRequestDTO {

    private Long idConta;
    private Long idTipoInvestimento;
    private BigDecimal valorAplicado;

    public AplicarInvestimentoRequestDTO() {}

    public Long getIdConta() { return idConta; }
    public void setIdConta(Long idConta) { this.idConta = idConta; }

    public Long getIdTipoInvestimento() { return idTipoInvestimento; }
    public void setIdTipoInvestimento(Long idTipoInvestimento) { this.idTipoInvestimento = idTipoInvestimento; }

    public BigDecimal getValorAplicado() { return valorAplicado; }
    public void setValorAplicado(BigDecimal valorAplicado) { this.valorAplicado = valorAplicado; }  
}
