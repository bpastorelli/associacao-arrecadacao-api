package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;

public class VinculoResidenciaDto {
	
	private List<VinculoResidencia> vinculos;
	
	public VinculoResidenciaDto() {
		
	}
	
	public List<VinculoResidencia> getVinculos() {
		
		return vinculos;
	}
	
	public void setVinculos(List<VinculoResidencia> vinculos) {
		
		this.vinculos = vinculos;
	}
}
