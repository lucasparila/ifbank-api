package com.ifbank.api_ifbank.model.DTO.investimento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestimentoDTO {

    private Long id;
    private String tipoInvestimento;
    private BigDecimal valorAplicado;
    private BigDecimal rendimentoAcumulado;
    private BigDecimal taxaRendimento;
    private LocalDate dataAplicacao;
    private String status;

    public InvestimentoDTO() {}

    public InvestimentoDTO(Long id, String tipoInvestimento, BigDecimal valorAplicado,
                           BigDecimal rendimentoAcumulado, BigDecimal taxaRendimento,
                           LocalDate dataAplicacao, String status) {
        this.id = id;
        this.tipoInvestimento = tipoInvestimento;
        this.valorAplicado = valorAplicado;
        this.rendimentoAcumulado = rendimentoAcumulado;
        this.taxaRendimento = taxaRendimento;
        this.dataAplicacao = dataAplicacao;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoInvestimento() { return tipoInvestimento; }
    public void setTipoInvestimento(String tipoInvestimento) { this.tipoInvestimento = tipoInvestimento; }

    public BigDecimal getValorAplicado() { return valorAplicado; }
    public void setValorAplicado(BigDecimal valorAplicado) { this.valorAplicado = valorAplicado; }

    public BigDecimal getRendimentoAcumulado() { return rendimentoAcumulado; }
    public void setRendimentoAcumulado(BigDecimal rendimentoAcumulado) { this.rendimentoAcumulado = rendimentoAcumulado; }

    public BigDecimal getTaxaRendimento() { return taxaRendimento; }
    public void setTaxaRendimento(BigDecimal taxaRendimento) { this.taxaRendimento = taxaRendimento; }

    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}