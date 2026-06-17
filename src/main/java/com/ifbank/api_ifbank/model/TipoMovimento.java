package com.ifbank.api_ifbank.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TIPOSMOVIMENTO")
public class TipoMovimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIPO_MOVIMENTO", nullable = false, length = 50)
    private String tipoMovimento;

    public TipoMovimento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoMovimento() { return tipoMovimento; }
    public void setTipoMovimento(String tipoMovimento) { this.tipoMovimento = tipoMovimento; }
}