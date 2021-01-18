package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.associacao.arrecadacao.api.entities.Visitante;

public class CadastroVisitanteDto {
	
	private List<Visitante> visitantes;
	
	public CadastroVisitanteDto() {
		
	}
	
	@NotEmpty(message = "O campo visitantes deve conter ao menos um visitante.")
	public List<Visitante> getVisitantes() {
		return visitantes;
	}

	public void setVisitantes(List<Visitante> visitantes) {
		this.visitantes = visitantes;
	}

}
