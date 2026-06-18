package com.ifbank.api_ifbank.model.DTO.movimentacao;

import java.math.BigDecimal;

public class ExtratoFiltroDTO {

    private Long idConta;
    private String nome;
    private BigDecimal valor;
    private Integer pagina = 0;
    private Integer tamanho = 10;
    private String ordenacao = "dataMovimento";
    private String direcao = "DESC";

    public ExtratoFiltroDTO() {}

    public Long getIdConta() { return idConta; }
    public void setIdConta(Long idConta) { this.idConta = idConta; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Integer getPagina() { return pagina; }
    public void setPagina(Integer pagina) { this.pagina = pagina; }

    public Integer getTamanho() { return tamanho; }
    public void setTamanho(Integer tamanho) { this.tamanho = tamanho; }

    public String getOrdenacao() { return ordenacao; }
    public void setOrdenacao(String ordenacao) { this.ordenacao = ordenacao; }

    public String getDirecao() { return direcao; }
    public void setDirecao(String direcao) { this.direcao = direcao; }
}