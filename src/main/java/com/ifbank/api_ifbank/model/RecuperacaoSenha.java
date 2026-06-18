package com.ifbank.api_ifbank.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RecuperacaoSenha")
public class RecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDate dataExpiracao;

    @Column(name = "usado", columnDefinition = "CHAR(1)")
    private String usado = "N";

    public RecuperacaoSenha() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDate getDataExpiracao() { return dataExpiracao; }
    public void setDataExpiracao(LocalDate dataExpiracao) { this.dataExpiracao = dataExpiracao; }

    public String getUsado() { return usado; }
    public void setUsado(String usado) { this.usado = usado; }
}