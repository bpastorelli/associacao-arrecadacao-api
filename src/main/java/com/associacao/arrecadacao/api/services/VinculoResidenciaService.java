package com.associacao.arrecadacao.api.services;

import java.util.List;
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
	 * Buscar por Morador ID
	 * 
	 * @param MoradorId
	 * @return Optional<VinculoResidencia>
	 */
	Optional<VinculoResidencia> buscarPorMoradorId(Long moradorId);
	
	/**
	 * Buscar por Residencia ID
	 * 
	 * @param residenciaId
	 * @return List<VinculoResidencia>
	 */
	List<VinculoResidencia> buscarPorResidenciaId(Long residenciaId);
	
	/**
	 * Persistir um vínculo de residência
	 * 
	 * @param vinculoResidencia
	 * @return VinculoResidencia
	 */
	List<VinculoResidencia> persistir(List<VinculoResidencia> vinculoResidencia);

}
