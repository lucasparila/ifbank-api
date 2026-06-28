package com.ifbank.api_ifbank.model.DTO.extrato;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoResponseDTO;

public class ExtratoPdfResponseDTO {

    private String nomeTitular;

    private String numeroConta;

    private BigDecimal saldoAtual;

    private LocalDateTime dataEmissao;

    private List<MovimentacaoResponseDTO> movimentacoes;

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public List<MovimentacaoResponseDTO> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<MovimentacaoResponseDTO> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }
	
}
