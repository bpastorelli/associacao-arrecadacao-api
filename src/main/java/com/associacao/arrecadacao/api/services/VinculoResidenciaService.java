package com.associacao.arrecadacao.api.services;

import java.util.Optional;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;

public interface VinculoResidenciaService {
	
	/**
	 * Buscar por Residencia ID e Morador ID
	 * 
	 * @param residenciaID
	 * @param moradorId
	 * @return VinculoResidencia
	 */
	Optional<VinculoResidencia> buscarPorResidenciaIdAndMoradorId(Long residenciaID, Long moradorId);
	
	/**
	 * Persistir um vínculo de residência
	 * 
	 * @param vinculoResidencia
	 * @return VinculoResidencia
	 */
	Optional<VinculoResidencia> persistir(VinculoResidencia vinculoResidencia);

}
