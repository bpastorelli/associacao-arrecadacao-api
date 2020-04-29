package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;


public class MoradorResponse implements Serializable {

	private static final long serialVersionUID = -5754246207015712518L;
	
	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private String rg;
	private String telefone;
	private String celular;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private Long residenciaId;

	public MoradorResponse() {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "cpf", nullable = true)
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getRg() {
		return rg;
	}
	
	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

    public Optional<Long> getResidenciaId() {
    	
    	return Optional.ofNullable(residenciaId);
    }
    
    public void setResidenciaId(Long residenciaId) {
    	
    	this.residenciaId = residenciaId;
    }
    
	@Override
	public String toString() {
		return "Morador [id=" + id + ", nome=" + nome + ", email=" + email + ", cpf=" + cpf
				+ ", rg=" + rg + ", telefone=" + telefone + ", celular=" + celular + ", dataCriacao="
				+ dataCriacao + ", dataAtualizacao=" + dataAtualizacao + "]";
	}

}
