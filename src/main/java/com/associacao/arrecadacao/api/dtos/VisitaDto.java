package com.associacao.arrecadacao.api.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;

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

	
	@CPF(message = "CPF inválido!")
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	@NotEmpty(message = "Não foi informada uma residência de destino de visita!")
	public Long getResidenciaId() {
		return residenciaId;
	}
	
	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
	}

	@Length(min = 8, max = 8, message = "Placa deve conter 8 caracteres no formato ___-____.")
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

}
