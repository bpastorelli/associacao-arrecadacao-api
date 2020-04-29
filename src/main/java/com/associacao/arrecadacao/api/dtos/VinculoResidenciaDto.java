package com.associacao.arrecadacao.api.dtos;

import java.util.Optional;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;

public class VinculoResidenciaDto {
	
	private Optional<VinculoResidencia> vinculo;
	
	public VinculoResidenciaDto() {
		
	}
	
	public Optional<VinculoResidencia> getVinculos() {
		
		return vinculo;
	}
	
	public void setVinculos(Optional<VinculoResidencia> vinculo) {
		
		this.vinculo = vinculo;
	}
}
