package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.ResidenciaResponse;

public class VinculosResidenciaResponseDto {
	

	private ResidenciaResponse residencia;
	
	private List<Morador> moradores;
	
	public VinculosResidenciaResponseDto() {
		
	}

	public ResidenciaResponse getResidencia() {
		return residencia;
	}

	public void setResidencia(ResidenciaResponse residencia) {
		this.residencia = residencia;
	}

	public List<Morador> getMoradores() {
		return moradores;
	}

	public void setMoradores(List<Morador> moradores) {
		this.moradores = moradores;
	}
	

}
