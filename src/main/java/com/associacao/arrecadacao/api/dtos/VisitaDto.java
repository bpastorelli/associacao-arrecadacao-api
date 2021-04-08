package com.associacao.arrecadacao.api.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class VisitaDto {
	
	private String rg;
	private String cpf;
	private Long residenciaId;
	private String placa;
	
	@NotEmpty(message = "Campo RG é obrigatório!")
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

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

}
