package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.Veiculo;

public class VeiculosVisitanteDto {
	
	private List<Veiculo> veiculos;
	
	public VeiculosVisitanteDto() {
		
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

}
