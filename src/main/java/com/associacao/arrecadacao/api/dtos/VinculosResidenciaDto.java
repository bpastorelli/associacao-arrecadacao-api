package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;

public class VinculosResidenciaDto {
	
	private List<VinculoResidencia> vinculos;
	
	public VinculosResidenciaDto() {
		
	}
	
	public List<VinculoResidencia> getVinculos() {
		
		return vinculos;
	}
	
	public void setVinculos(List<VinculoResidencia> vinculos) {
		
		this.vinculos = vinculos;
	}
}
