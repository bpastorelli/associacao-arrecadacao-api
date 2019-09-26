package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;

public class VinculoResidenciaDto {
	
	private List<VinculoResidencia> vinculoResidencia;
	
	public VinculoResidenciaDto() {
		
	}
	
	@NotEmpty(message = "Você deve informar ao menos um vinculo.")
	public List<VinculoResidencia> getVinculoResidencia() {
		
		return vinculoResidencia;
	}
	
	public void setVinculoReidencia(List<VinculoResidencia> vinculoResidencia) {
		
		this.vinculoResidencia = vinculoResidencia;
	}
}
