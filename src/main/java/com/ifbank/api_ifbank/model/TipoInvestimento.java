package com.ifbank.api_ifbank.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TiposInvestimento")
public class TipoInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "rentabilidade_mes")
    private BigDecimal rentabilidadeMes;

    @Column(name = "carencia_dias")
    private Integer carenciaDias;

    @Column(name = "valorMinimo")
    private BigDecimal valorMinimo;

    public TipoInvestimento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getRentabilidadeMes() { return rentabilidadeMes; }
    public void setRentabilidadeMes(BigDecimal rentabilidadeMes) { this.rentabilidadeMes = rentabilidadeMes; }

    public Integer getCarenciaDias() { return carenciaDias; }
    public void setCarenciaDias(Integer carenciaDias) { this.carenciaDias = carenciaDias; }

    public BigDecimal getValorMinimo() { return valorMinimo; }
    public void setValorMinimo(BigDecimal valorMinimo) { this.valorMinimo = valorMinimo; }

    
} 
