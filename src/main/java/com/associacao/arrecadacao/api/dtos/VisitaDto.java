package com.associacao.arrecadacao.api.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class VisitaDto {
	
	private String rg;
	private Long residenciaId;
	
	
	@NotEmpty(message = "O campo RG é obrigatório")
	public String getRg() {
		return rg;
	}
	
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public Long getResidenciaId() {
		return residenciaId;
	}
	
	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
	}

}
