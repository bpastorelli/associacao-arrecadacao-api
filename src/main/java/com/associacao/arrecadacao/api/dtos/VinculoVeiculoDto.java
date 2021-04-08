package com.associacao.arrecadacao.api.dtos;

public class VinculoVeiculoDto {
	
	private Long veiculoId;
	private Long visitanteId;
	
	public VinculoVeiculoDto() {	
		
	}

	public Long getVeiculoId() {
		return veiculoId;
	}

	public void setVeiculoId(Long veiculoId) {
		this.veiculoId = veiculoId;
	}

	public Long getVisitanteId() {
		return visitanteId;
	}

	public void setVisitanteId(Long visitanteId) {
		this.visitanteId = visitanteId;
	}

}
