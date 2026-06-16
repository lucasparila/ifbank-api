package com.ifbank.api_ifbank.model.DTO.investimento;

import java.math.BigDecimal;

public class ResumoInvestimentoDTO {

    private BigDecimal totalInvestido;
    private BigDecimal rendimentoAcumulado;
    private Integer quantidadeInvestimentos;

    public ResumoInvestimentoDTO() {}

    public ResumoInvestimentoDTO(BigDecimal totalInvestido, BigDecimal rendimentoAcumulado, Integer quantidadeInvestimentos) {
        this.totalInvestido = totalInvestido;
        this.rendimentoAcumulado = rendimentoAcumulado;
        this.quantidadeInvestimentos = quantidadeInvestimentos;
    }

    public BigDecimal getTotalInvestido() { return totalInvestido; }
    public void setTotalInvestido(BigDecimal totalInvestido) { this.totalInvestido = totalInvestido; }

    public BigDecimal getRendimentoAcumulado() { return rendimentoAcumulado; }
    public void setRendimentoAcumulado(BigDecimal rendimentoAcumulado) { this.rendimentoAcumulado = rendimentoAcumulado; }

    public Integer getQuantidadeInvestimentos() { return quantidadeInvestimentos; }
    public void setQuantidadeInvestimentos(Integer quantidadeInvestimentos) { this.quantidadeInvestimentos = quantidadeInvestimentos; }
}