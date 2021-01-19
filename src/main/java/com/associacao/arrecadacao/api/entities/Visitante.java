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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "visitante")
public class Visitante implements Serializable {
	
	private static final long serialVersionUID = -5754246207015712519L;
	
	private Long   id;
	private String nome;
	private String cpf;
	private String rg;
	private String endereco;
	private String numero;
	private String cep;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	private String telefone;
	private String celular;
	private Date   dataCriacao;
	private Date   dataAtualizacao;
	private Long   posicao;
	private List<Visita> visitas;
	
	
	public Visitante(){		
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "nome", nullable = false)
	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	@Column(name = "cpf", nullable = true)
	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	
	@Column(name = "rg", nullable = false)
	public String getRg() {
		return rg;
	}


	public void setRg(String rg) {
		this.rg = rg;
	}


	@Column(name="telefone", nullable = true)
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Column(name = "celular", nullable = false)
	public String getCelular() {
		return celular;
	}
	
	public void setCelular(String celular) {
		this.celular = celular;
	}

	@Column(name = "endereco", nullable = true)
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@Column(name = "numero", nullable = true)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "cep", nullable = true)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Column(name = "complemento", nullable = true)
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@Column(name = "bairro", nullable = true)
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	@Column(name = "cidade", nullable = true)
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@Column(name = "uf", nullable = true)
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@OneToMany(mappedBy = "visitante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Visita> getVisitas() {
		return visitas;
	}

	public void setVisitas(List<Visita> visitas) {
		this.visitas = visitas;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_atualizacao", nullable = false)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}


	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	
	@Column(name = "posicao", nullable = false)
	public Long getPosicao() {
		return posicao;
	}


	public void setPosicao(Long posicao) {
		this.posicao = posicao;
	}
	
	@PreUpdate
	public void preUpdate() {
		
		dataAtualizacao = new Date();
		
	}
	
	@PrePersist
	public void prePersist() {
		
        final Date atual = new Date();
        final long status = 1;
        dataCriacao = atual;
        dataAtualizacao = atual;
        posicao = status;
        
	}
	
	
	@Override
	public String toString() {
		return "Morador [id=" + id + ", nome=" + nome + ", cpf=" + cpf
				+ ", rg=" + rg + ", telefone=" + telefone + ", celular=" + celular + ", cep=" + cep + ", endereco=" + endereco + ""
				+ ", numero=" + numero + ", bairro=" + bairro + ", complemento=" + complemento + ", cidade=" + cidade + ", uf=" + uf + ", dataCriacao="
				+ dataCriacao + ", dataAtualizacao=" + dataAtualizacao + ", posicao=" + posicao + "]";
	}
	
}
