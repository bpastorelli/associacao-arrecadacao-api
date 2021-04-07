package com.associacao.arrecadacao.api.dtos;

import com.associacao.arrecadacao.api.entities.Veiculo;
import com.associacao.arrecadacao.api.entities.Visitante;

public class VinculoVeiculoDto {
	
	private Veiculo   veiculo;
	private Visitante visitante;
	
	public VinculoVeiculoDto() {
		
		
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}

}
