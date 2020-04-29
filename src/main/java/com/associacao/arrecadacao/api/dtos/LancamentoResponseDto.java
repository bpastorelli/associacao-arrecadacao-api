package com.associacao.arrecadacao.api.dtos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;

public class LancamentoResponseDto {
	
	private Long id;
	private String dataPagamento;
	private String periodo;
	private BigDecimal valor;
	private String dataCriacao;
	private String dataAtualizacao;
	private Long residenciaId;
	
	public LancamentoResponseDto() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
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

	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	
	public Long getResidenciaId() {
		return residenciaId;
	}

	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
	}

}
