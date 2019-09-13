package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;

	private Long id;
	private Date dataPagamento;
	private String periodo;
	private BigDecimal valor;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private Long usuarioRecebimento;
	private Morador morador;
	private Long residenciaId;
	
	public Lancamento() {
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_pagamento", nullable = false)
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	@Column(name = "periodo", nullable = false)
	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	@Column(name = "valor", nullable = false)
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	@ManyToOne(fetch = FetchType.EAGER)
	public Morador getMorador() {
		return morador;
	}

	public void setMorador(Morador morador) {
		this.morador = morador;
	}
	
	@Column(name = "usuario_recebimento", nullable = false)
	public Long getUsuarioRecebimento() {
		return usuarioRecebimento;
	}

	public void setUsuarioRecebimento(Long usuarioRecebimento) {
		this.usuarioRecebimento = usuarioRecebimento;
	}
	
	@Column(name = "residencia_id", nullable = false)
	public Long getResidenciaId() {
		return residenciaId;
	}

	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
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
		return "Lancamento [id=" + id + ", dataPagamento=" + dataPagamento + ", periodo=" + periodo + ", valor=" + valor
				+ ", dataCriacao=" + dataCriacao + ", dataAtualizacao=" + dataAtualizacao + ", morador=" + morador + ", residenciaId=" + residenciaId + ", usuarioRecebimento=" + usuarioRecebimento + "]";
	}

}