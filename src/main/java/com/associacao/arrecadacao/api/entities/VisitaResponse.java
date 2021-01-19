package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;


public class VisitaResponse implements Serializable {
	
	private static final long serialVersionUID = -5754246207015712520L;
	
	private Long   id;
	private String nome;
	private String rg;
	private String dataEntrada;
	private String horaEntrada;
	private Long   residenciaId;
	
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
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	public String getDataEntrada() {
		return dataEntrada;
	}
	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	public String getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public Long getResidenciaId() {
		return residenciaId;
	}
	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
	}
	
	

}
