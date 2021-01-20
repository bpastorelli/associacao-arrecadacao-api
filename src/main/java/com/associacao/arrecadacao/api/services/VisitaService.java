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
	 * Buscar por RG
	 * @param rg
	 * @return
	 */
	List<Visita> buscarPorIdOrRgOrCpfAndPosicao(Long id, String rg, String cpf, Long posicao);

}
