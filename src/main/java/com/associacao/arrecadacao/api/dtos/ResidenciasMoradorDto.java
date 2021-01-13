package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.ResidenciaResponse;

public class ResidenciasMoradorDto {
	
	private List<ResidenciaResponse> residencias;
	
	public ResidenciasMoradorDto() {
		
	}

	public List<ResidenciaResponse> getResidencias() {
		return residencias;
	}

	public void setResidencias(List<ResidenciaResponse> residencias) {
		this.residencias = residencias;
	}

}
