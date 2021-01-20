package com.associacao.arrecadacao.api.services;

import java.util.List;

import com.associacao.arrecadacao.api.entities.Visita;

public interface VisitaService {
	
	/**
	 * Persistir uma visita
	 * @param visita
	 * @return
	 */
	Visita persistir(Visita visita);
	
	/**
	 * Buscar por id
	 * @param id
	 * @return
	 */
	Visita buscarPorId(Long id);
	
	/**
	 * Buscar por RG
	 * @param rg
	 * @return
	 */
	List<Visita> buscarPorPosicaoOrRgOrCpf(Integer posicao, String rg, String cpf);

}
