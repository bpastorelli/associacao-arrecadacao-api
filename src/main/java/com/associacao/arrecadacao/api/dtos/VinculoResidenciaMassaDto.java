package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.VinculoResidenciaMassa;

public class VinculoResidenciaMassaDto {
	
	private List<VinculoResidenciaMassa> vinculosMassa;
	
	public VinculoResidenciaMassaDto() {
		
	}

	public List<VinculoResidenciaMassa> getVinculosMassa() {
		return vinculosMassa;
	}

	public void setVinculosMassa(List<VinculoResidenciaMassa> vinculosMassa) {
		this.vinculosMassa = vinculosMassa;
	}

	
}
