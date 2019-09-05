package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "residencia")
public class Residencia implements Serializable {

	private static final long serialVersionUID = 3960436649365666213L;
	
	private Long   id;
	private String iptu;
	private String endereco;
	private String numero;
	private String bairro;
	private String cep;
	private String cidade;
	private String uf;
	private Date   dataCriacao;
	private Date   dataAtualizacao;
	private List<Morador> moradores;
	
	public Residencia() {
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "iptu", nullable = true)
	public String getIptu() {
		return iptu;
	}

	public void setIptu(String iptu) {
		this.iptu = iptu;
	}

	@Column(name = "endereco", nullable = false)
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@Column(name = "numero", nullable = false)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@Column(name = "bairro", nullable = false)
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}	
	
	@Column(name = "cep", nullable = false)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}	
	
	@Column(name = "cidade", nullable = false)
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}	
	
	@Column(name = "uf", nullable = false)
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}	
	
	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "data_atualizacao", nullable = false)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@OneToMany(mappedBy = "residencia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Morador> getMoradores() {
		return moradores;
	}

	public void setFuncionarios(List<Morador> moradores) {
		this.moradores = moradores;
	}
	
	@PreUpdate
    public void preUpdate() {
        dataAtualizacao = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataCriacao = atual;
        dataAtualizacao = atual;
    }

	@Override
	public String toString() {
		return "Residencia [id=" + id + ", iptu=" + iptu + ", endereco=" + endereco + ", numero=" + numero + ", bairro=" + bairro + ", cidade=" + cidade + ", uf=" + uf + ", dataCriacao=" + dataCriacao
				+ ", dataAtualizacao=" + dataAtualizacao + "]";
	}
}
