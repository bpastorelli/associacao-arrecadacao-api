package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.Morador;

public class MoradoresResidenciaDto {
	
	private List<Morador> moradores;
	
	public MoradoresResidenciaDto() {
		
	}

	public List<Morador> getMoradores() {
		return moradores;
	}

	public void setMoradores(List<Morador> moradores) {
		this.moradores = moradores;
	}

}
