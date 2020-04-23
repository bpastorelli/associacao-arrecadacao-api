package com.associacao.arrecadacao.api.dtos;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

public class LancamentoResponseDto {
	
	private Long id;
	private Date dataPagamento;
	private String periodo;
	private BigDecimal valor;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private Long residenciaId;
	
	public LancamentoResponseDto() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	@NotEmpty(message = "Periodo não pode ser nulo")
	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	@NotEmpty(message = "Valor não pode ser nulo")
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
	
	public Long getResidenciaId() {
		return residenciaId;
	}

	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
	}

}
