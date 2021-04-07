package com.associacao.arrecadacao.api.services;

import java.util.List;
import java.util.Optional;

import com.associacao.arrecadacao.api.entities.VinculoVeiculo;

public interface VinculoVeiculoService {
	
	/**
	 * Buscar por Veiculo ID e Visitante ID
	 * 
	 * @param veiculoId
	 * @param visitanteId
	 * @return VinculoVeiculo
	 */
	Optional<VinculoVeiculo> buscarPorVeiculoIdAndVisitanteId(Long veiculoId, Long visitanteId);
	
	/**
	 * Buscar por Visitante ID
	 * 
	 * @param visitanteId
	 * @return List<VinculoVeiculo>
	 */
	List<VinculoVeiculo> buscarPorVisitanteId(Long visitanteId);
	
	/**
	 * Buscar por Veiculo ID
	 * 
	 * @param VeiculoId
	 * @return List<VinculoVeiculo>
	 */
	List<VinculoVeiculo> buscarPorVeiculoId(Long veiculoId);
	
	/**
	 * Persistir um vínculo de veiculo
	 * 
	 * @param vinculoVeiculo
	 * @return List<VinculoVeiculo>
	 */
	List<VinculoVeiculo> persistir(List<VinculoVeiculo> vinculoVeiculo);

	/**
	 * Remove um vinculo de visitante a veiculo
	 * 
	 * @param id
	 */
	void remover(Long id);

}
