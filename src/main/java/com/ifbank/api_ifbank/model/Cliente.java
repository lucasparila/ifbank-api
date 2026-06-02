package com.ifbank.api_ifbank.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Clientes")
public class Cliente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@NotNull(message = "O nome é obrigatório")
	@Column(name = "nome")
    private String nome;
	
	@NotNull(message = "A data de nascimento é obrigatório")
	@Column(name= "data_nascimento")
    private LocalDate dataNascimento;
	
	@Column(name = "data_cadastro")
    private LocalDate dataCadastro;
    
	@Column(name= "foto_url")
	private String fotoUrl;

	@OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
    
    @OneToOne
    @JoinColumn(name = "id_endereco", referencedColumnName = "id", nullable = false)
    private Endereco endereco;
    
    @OneToOne
    @JoinColumn(name = "id_telefone", referencedColumnName = "id", nullable = false)
    private Telefone telefone;
    
    public Cliente() {}

	public Cliente(Long id, @NotNull(message = "O nome é obrigatório") String nome,
			@NotNull(message = "A data de nascimento é obrigatório") LocalDate dataNascimento, LocalDate dataCadastro, String fotoUrl,
			Usuario usuario, Endereco endereco, Telefone telefone) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.dataCadastro = dataCadastro;
		this.fotoUrl = fotoUrl;
		this.usuario = usuario;
		this.endereco = endereco;
		this.telefone = telefone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}
	public String getFotoUrl() {
		return fotoUrl;
	}

	public void setFotoUrl(String fotoUrl) {
		this.fotoUrl = fotoUrl;
	}
    
    
}
