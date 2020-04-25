package com.associacao.arrecadacao.api.services;

import java.util.List;
import java.util.Optional;

import com.associacao.arrecadacao.api.entities.VinculoResidenciaMassa;

public interface VinculoResidenciaMassaService {
	
	/**
	 * Buscar por Residencia ID e Morador ID
	 * 
	 * @param residenciaID
	 * @param moradorId
	 * @return VinculoResidenciaMassa
	 */
	Optional<VinculoResidenciaMassa> buscarPorResidenciaIdAndMoradorId(Long residenciaID, Long moradorId);
	
	/**
	 * Buscar por Morador ID
	 * 
	 * @param MoradorId
	 * @return List<VinculoResidenciaMassa>
	 */
	List<VinculoResidenciaMassa> buscarPorMoradorId(Long moradorId);
	
	/**
	 * Buscar por Residencia ID
	 * 
	 * @param residenciaId
	 * @return List<VinculoResidencia>
	 */
	List<VinculoResidenciaMassa> buscarPorResidenciaId(Long residenciaId);
	
	/**
	 * Persistir um vínculo de residência
	 * 
	 * @param vinculoResidencia
	 * @return List<VinculoResidencia>
	 */
	List<VinculoResidenciaMassa> persistir(List<VinculoResidenciaMassa> vinculoResidencia);

}
