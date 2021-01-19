package com.associacao.arrecadacao.api.dtos;


public class VisitaDto {
	
	private String rg;
	private String cpf;
	private Long residenciaId;
	
	public String getRg() {
		return rg;
	}
	
	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public Long getResidenciaId() {
		return residenciaId;
	}
	
	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
	}

}
